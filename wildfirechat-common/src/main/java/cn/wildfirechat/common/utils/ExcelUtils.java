package cn.wildfirechat.common.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Excel工具类
 *
 * @author huangxk
 * @version [版本号, 2017-1-10](必须)
 * @see [相关类/方法](可选)
 * @since [产品/模块版本](必选)
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ExcelUtils {

    /**
     * sheet最大行数
     */
    private static final Integer MAX_PER_SHEET = 50000;

    /**
     * excel导出工具
     *
     * @param request  请求
     * @param response 响应
     * @param fileName 导出文件名称
     * @param map      标题列名
     * @param list     列值
     * @param <E>      泛型
     */
    public static <E> void export(HttpServletRequest request, HttpServletResponse response, String fileName,
                                  LinkedHashMap<String, String> map, List<E> list) {
        // 创建工作簿
        HSSFWorkbook wb = new HSSFWorkbook();

        // 数据分组
        List<List<E>> wrapList = ExcelUtils.groupListByQuantity(list, MAX_PER_SHEET);

        // 创建sheet
        if (wrapList.size() == 0) {
            createSheet(wb, map, new ArrayList<>());
        } else {
            for (List<E> wrap : wrapList) {
                createSheet(wb, map, wrap);
            }
        }

        OutputStream out = null;
        try {
            // 中文文件名进行编码
            String name = encodeChineseDownloadFileName(request, fileName);
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + name + ".xls");
            response.setContentType("application/vnd.ms-excel;charset=utf-8");

            out = response.getOutputStream();
            wb.write(out);

            // 刷新、关闭
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 创建sheet
     *
     * @param wb   工作薄
     * @param map  表头列
     * @param list 数据列
     * @param <E>  泛型
     */
    private static <E> void createSheet(HSSFWorkbook wb, LinkedHashMap<String, String> map, List<E> list) {
        // 创建一个sheet
        HSSFSheet sheet = wb.createSheet();
        // 自适应列宽度
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(1, true);

        HSSFRow headerRow = sheet.createRow(0);
        HSSFRow contentRow;

        // 每列最大宽度
        Map<Integer, Integer> widthMap = new HashMap<>();

        // 设置标题列
        int headerIndex = 0;
        for (String header : map.values()) {
            headerRow.createCell(headerIndex).setCellValue(header);

            widthMap.put(headerIndex, header.length() * 2 + 2);

            headerIndex++;
        }

        try {
            for (int i = 0; i < list.size(); i++) {
                contentRow = sheet.createRow(i + 1);

                // 获取每一个对象
                E o = list.get(i);
                Class cls = o.getClass();

                int columnIndex = 0;
                for (String key : map.keySet()) {
                    // 值
                    Object value;
                    double buil = 0;
                    StringBuilder str = new StringBuilder();
                    // Map取值
                    if (o instanceof HashMap) {
                        if (key.contains("/")) {
                            String[] fieldNameList = key.split("/");
                            for (int j = 0; j < fieldNameList.length; j++) {
                                Object fieldName = ((HashMap) o).get(fieldNameList[j]);
                                if (fieldName != null) {
                                    if (j != 0) {
                                        str.append("/");
                                    }
                                    str.append(fieldName);
                                }
                            }
                            value = str;
                        } else {
                            value = ((HashMap) o).get(key);
                        }
                    }
                    // 反射取值
                    else {
                        if (key.contains("/")) {
                            String[] fieldNameList = key.split("/");
                            for (String aFieldNameList : fieldNameList) {
                                String fieldName =
                                        aFieldNameList.substring(0, 1).toUpperCase() + aFieldNameList.substring(1);
                                Method getMethod = cls.getMethod("get" + fieldName);
                                value = getMethod.invoke(o);
                                if (value != null) {
                                    if (value instanceof BigDecimal) {
                                        buil += ((BigDecimal) value).doubleValue();
                                    } else if (value instanceof Double || value instanceof Integer) {
                                        buil += (double) value;
                                    }
                                }

                            }
                            value = buil;
                        } else {
                            String fieldName = key.substring(0, 1).toUpperCase() + key.substring(1);
                            Method getMethod = cls.getMethod("get" + fieldName);
                            value = getMethod.invoke(o);
                        }
                    }

                    // 列宽判断
                    if (value != null) {
                        int length = value.toString().split("\n")[0].length();
                        if (length > widthMap.get(columnIndex)) {
                            widthMap.put(columnIndex, length);
                        }
                    }

                    // 创建单元格
                    HSSFCell cell = contentRow.createCell(columnIndex);

                    // 为单元格赋值
                    setCellValue(cell, value);

                    columnIndex++;
                }
            }

            // 设置列宽
            for (Integer index : widthMap.keySet()) {
                // 富余2个字符串宽度
                sheet.setColumnWidth(index, (widthMap.get(index) + 2) * 256);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置Excel单元格值
     *
     * @param cell  Excel单元格
     * @param value 单元格值
     */
    private static void setCellValue(HSSFCell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
            return;
        }
        if (value instanceof Date) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            cell.setCellValue(df.format(value));
        } else if (value instanceof BigDecimal) {
            cell.setCellValue((new BigDecimal(value.toString())).doubleValue());
        } else if (value instanceof Integer) {
            cell.setCellValue(new Double(value.toString()));
        } else if (value instanceof Long) {
            cell.setCellValue(new Double(value.toString()));
        } else if (value instanceof Boolean) {
            cell.setCellValue(((Boolean) value) ? "是" : "否");
        } else {
            cell.setCellValue(String.valueOf(value));
        }
    }

    /**
     * 将集合按指定数量分组
     *
     * @param list     数据集合
     * @param quantity 分组数量
     * @param <T>      泛型
     * @return 分组结果
     */
    private static <T> List<List<T>> groupListByQuantity(List<T> list, int quantity) {
        if (list == null || list.size() == 0) {
            return new ArrayList<>();
        }

        if (quantity <= 0) {
            return new ArrayList<>();
            // new IllegalArgumentException("Wrong quantity.");
        }

        List<List<T>> wrapList = new ArrayList<>();
        int count = 0;
        while (count < list.size()) {
            List<T> subList = new ArrayList<>(list.subList(count, Math.min((count + quantity), list.size())));

            wrapList.add(subList);

            count += quantity;
        }

        return wrapList;
    }

    /**
     * 对文件流输出下载的中文文件名进行编码 屏蔽各种浏览器版本的差异性
     */
    private static String encodeChineseDownloadFileName(HttpServletRequest request, String fileName) {
        String agent = request.getHeader("USER-AGENT");
        String name = fileName;
        try {
            if (agent != null && agent.contains("MSIE")) {
                name = URLEncoder.encode(name, "utf-8");
            } else {
                name = new String(name.getBytes(StandardCharsets.UTF_8), "iso8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 1011; i++) {
            list.add(i);
        }

        List<List<Integer>> resultList = groupListByQuantity(list, 0);
        for (List<Integer> l : resultList) {
            System.out.println(l);
        }
    }

}
