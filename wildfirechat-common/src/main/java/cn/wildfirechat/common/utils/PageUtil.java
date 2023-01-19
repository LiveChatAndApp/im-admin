package cn.wildfirechat.common.utils;

/**
 * 分页查询(动态排序)工具类
 * 
 *
 */
public class PageUtil {

	public static final String DEFAULT_ORDER = "ASC";

	/**
	 * 产生ORDER BY SQL<br>
	 * 
	 * 多组排序使用冒号(:)分隔<br>
	 * 例: m.UpdateTime,ASC:m.CreateTime,DESC
	 * 
	 * @param sort
	 * @return
	 */
	public static String orderBySql(String sort) {
		String[] sortparam = (sort == null) ? null : sort.split(":");
		if (sortparam != null && sortparam.length > 0) {
			String[] columns = new String[sortparam.length];
			String[] orders = new String[sortparam.length];
			for (int i = 0; i < sortparam.length; i++) {
				if (sortparam[i] == null || sortparam[i].isEmpty()) {
					continue;
				}
				String[] columnSort = sortparam[i].split(",");
				if (columnSort != null && columnSort.length == 2) {
					columns[i] = columnSort[0];
					if (columnSort.length > 1) {
						orders[i] = columnSort[1];
					}
				}
			}
			return orderBySql(columns, orders);
		} else {
			return null;
		}
	}

	/**
	 * 产生ORDER BY SQL
	 * 
	 * @param columns
	 * @param orders
	 * @return
	 */
	public static String orderBySql(String[] columns, String[] orders) {
		if (columns == null || columns.length == 0) {
			return "";
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (int x = 0; x < columns.length; x++) {
			if (columns[x] == null) {
				continue;
			}
			String column = columns[x];
			String order = null;
			if (orders != null && orders.length > x) {
				order = orders[x].toUpperCase();
				if (!(order.equals("ASC") || order.equals("DESC"))) {
					throw new IllegalArgumentException("非法的排序策略：" + column);
				}
			} else {
				order = DEFAULT_ORDER;
			}
			// 检查是否有别名
			String alias = null;
			if(column.contains(".")) {
				String[] aliasColumn = column.split("[.]");
				alias = aliasColumn[0] + ".";
				column = aliasColumn[1];
			}
			// 判断列名称的合法性，防止SQL注入。只能是【字母，数字，下划线】
			if (!column.matches("[A-Za-z0-9_]+")) {
				throw new IllegalArgumentException("非法的排序字段名称：" + column);
			}
			if (x != 0) {
				stringBuilder.append(", ");
			}
			if(alias != null) {
				stringBuilder.append(alias);
			}
			stringBuilder.append("`" + column + "` " + order);
		}
		return stringBuilder.toString();
	}

	/**
	 * 驼峰转换为下划线
	 * 
	 * @param value
	 * @return
	 */
	public static String humpConversionUnderscore(String value) {
		StringBuilder stringBuilder = new StringBuilder();
		char[] chars = value.toCharArray();
		for (char charactor : chars) {
			if (Character.isUpperCase(charactor)) {
				stringBuilder.append("_");
				charactor = Character.toLowerCase(charactor);
			}
			stringBuilder.append(charactor);
		}
		return stringBuilder.toString();
	}
}
