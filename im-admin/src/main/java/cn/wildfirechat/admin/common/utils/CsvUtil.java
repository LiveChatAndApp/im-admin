package cn.wildfirechat.admin.common.utils;

import cn.wildfirechat.common.utils.FormatUtil;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

public final class CsvUtil {
    private CsvUtil() {
    }

    public static <T> void export(HttpServletResponse response,
                                  List<T> list,
                                  Class<T> clazz,
                                  String filename,
                                  Date startTime,
                                  Date endTime) throws IOException {

        String filenameDate = FormatUtil.formatDateTimeCompact();
        StringBuilder sb = new StringBuilder(filename + filenameDate);
        if (startTime != null) {
            sb.append(".")
                    .append(FormatUtil.formatDateTimeCompact(startTime));
            if (endTime != null) {
                sb.append("-")
                        .append(FormatUtil.formatDateTimeCompact(endTime));
            }
        }
        sb.append(".csv");


        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Encoding", "UTF-8");
        response.setHeader("Content-Type", "text/csv");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment;filename=" + sb);

        PrintWriter printWriter = response.getWriter();
        printWriter.write("\uFEFF");

        CsvMapper csvMapper = new CsvMapper();
        csvMapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        CsvSchema csvSchema = csvMapper.schemaFor(clazz)
                .withColumnReordering(false)
                .withHeader()
                .withColumnSeparator(',');
        csvMapper.writer(csvSchema).writeValue(printWriter, list);
    }
}
