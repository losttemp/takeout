package com.baidu.iov.dueros.waimai.net.entity.response;

import java.util.List;

public class FilterConditionResponse {

	private MeituanBean meituan;
	private IovBean iov;

	public MeituanBean getMeituan() {
		return meituan;
	}

	public void setMeituan(MeituanBean meituan) {
		this.meituan = meituan;
	}

	public IovBean getIov() {
		return iov;
	}

	public void setIov(IovBean iov) {
		this.iov = iov;
	}

	@Override
	public String toString() {
		return "FilterConditionResponse{" +
				"meituan=" + meituan +
				", iov=" + iov +
				'}';
	}

	public static class MeituanBean {

		private long code;
		private String msg;
		private Object errorInfo;
		private DataBean data;

		public long getCode() {
			return code;
		}

		public void setCode(long code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public Object getErrorInfo() {
			return errorInfo;
		}

		public void setErrorInfo(Object errorInfo) {
			this.errorInfo = errorInfo;
		}

		public DataBean getData() {
			return data;
		}

		public void setData(DataBean data) {
			this.data = data;
		}

		@Override
		public String toString() {
			return "MeituanBean{" +
					"code=" + code +
					", msg='" + msg + '\'' +
					", errorInfo=" + errorInfo +
					", data=" + data +
					'}';
		}

		public static class DataBean {
			private List<CategoryFilterListBean> category_filter_list;
			private List<SortTypeListBean> sort_type_list;
			private List<ActivityFilterListBean> activity_filter_list;

			public List<CategoryFilterListBean> getCategory_filter_list() {
				return category_filter_list;
			}

			public void setCategory_filter_list(List<CategoryFilterListBean>
														category_filter_list) {
				this.category_filter_list = category_filter_list;
			}

			public List<SortTypeListBean> getSort_type_list() {
				return sort_type_list;
			}

			public void setSort_type_list(List<SortTypeListBean> sort_type_list) {
				this.sort_type_list = sort_type_list;
			}

			public List<ActivityFilterListBean> getActivity_filter_list() {
				return activity_filter_list;
			}

			public void setActivity_filter_list(List<ActivityFilterListBean>
														activity_filter_list) {
				this.activity_filter_list = activity_filter_list;
			}

			@Override
			public String toString() {
				return "DataBean{" +
						"category_filter_list=" + category_filter_list +
						", sort_type_list=" + sort_type_list +
						", activity_filter_list=" + activity_filter_list +
						'}';
			}

			public static class CategoryFilterListBean {

				private long code;
				private String name;
				private int quantity;
				private List<SubCategoryListBean> sub_category_list;

				public long getCode() {
					return code;
				}

				public void setCode(long code) {
					this.code = code;
				}

				public String getName() {
					return name;
				}

				public void setName(String name) {
					this.name = name;
				}

				public int getQuantity() {
					return quantity;
				}

				public void setQuantity(int quantity) {
					this.quantity = quantity;
				}

				public List<SubCategoryListBean> getSub_category_list() {
					return sub_category_list;
				}

				public void setSub_category_list(List<SubCategoryListBean> sub_category_list) {
					this.sub_category_list = sub_category_list;
				}

				@Override
				public String toString() {
					return "CategoryFilterListBean{" +
							"code=" + code +
							", name='" + name + '\'' +
							", quantity=" + quantity +
							", sub_category_list=" + sub_category_list +
							'}';
				}

				public static class SubCategoryListBean {
					private long code;
					private String name;
					private int quantity;
					private String icon_url;

					public long getCode() {
						return code;
					}

					public void setCode(long code) {
						this.code = code;
					}

					public String getName() {
						return name;
					}

					public void setName(String name) {
						this.name = name;
					}

					public int getQuantity() {
						return quantity;
					}

					public void setQuantity(int quantity) {
						this.quantity = quantity;
					}

					public String getIcon_url() {
						return icon_url;
					}

					public void setIcon_url(String icon_url) {
						this.icon_url = icon_url;
					}

					@Override
					public String toString() {
						return "SubCategory{" +
								"code=" + code +
								", name='" + name + '\'' +
								", quantity=" + quantity +
								", icon_url='" + icon_url + '\'' +
								'}';
					}
				}
				
				
			}

			public static class SortTypeListBean {

				private long code;
				private String name;
				private String short_name;
				private String icon_url;
				private String icon_url_click;
				private int position;

				public  static final int TABPOS=0;
				public  static final int LISTPOS=1;

				public long getCode() {
					return code;
				}

				public void setCode(long code) {
					this.code = code;
				}

				public String getName() {
					return name;
				}

				public void setName(String name) {
					this.name = name;
				}

				public String getShort_name() {
					return short_name;
				}

				public void setShort_name(String short_name) {
					this.short_name = short_name;
				}

				public String getIcon_url() {
					return icon_url;
				}

				public void setIcon_url(String icon_url) {
					this.icon_url = icon_url;
				}

				public String getIcon_url_click() {
					return icon_url_click;
				}

				public void setIcon_url_click(String icon_url_click) {
					this.icon_url_click = icon_url_click;
				}

				public int getPosition() {
					return position;
				}

				public void setPosition(int position) {
					this.position = position;
				}

				@Override
				public String toString() {
					return "SortTypeListBean{" +
							"code=" + code +
							", name='" + name + '\'' +
							", short_name='" + short_name + '\'' +
							", icon_url='" + icon_url + '\'' +
							", icon_url_click='" + icon_url_click + '\'' +
							", position=" + position +
							'}';
				}
			}

			public static class ActivityFilterListBean {

				private String group_title;
				private int support_multi_choice;
				private int display_style;
				private List<ItemsBean> items;

				public String getGroup_title() {
					return group_title;
				}

				public void setGroup_title(String group_title) {
					this.group_title = group_title;
				}

				public int getSupport_multi_choice() {
					return support_multi_choice;
				}

				public void setSupport_multi_choice(int support_multi_choice) {
					this.support_multi_choice = support_multi_choice;
				}

				public int getDisplay_style() {
					return display_style;
				}

				public void setDisplay_style(int display_style) {
					this.display_style = display_style;
				}

				public List<ItemsBean> getItems() {
					return items;
				}

				public void setItems(List<ItemsBean> items) {
					this.items = items;
				}

				@Override
				public String toString() {
					return "ActivityFilterListBean{" +
							"group_title='" + group_title + '\'' +
							", support_multi_choice=" + support_multi_choice +
							", display_style=" + display_style +
							", items=" + items +
							'}';
				}

				public static class ItemsBean {

					private long code;
					private String name;
					private String icon;
					private String remarks;
					private BubbleInfoBean bubble_info;
					private List<?> aggregated_activity_codes;
					private boolean isChcked;

					public long getCode() {
						return code;
					}

					public void setCode(long code) {
						this.code = code;
					}

					public String getName() {
						return name;
					}

					public void setName(String name) {
						this.name = name;
					}

					public String getIcon() {
						return icon;
					}

					public void setIcon(String icon) {
						this.icon = icon;
					}

					public String getRemarks() {
						return remarks;
					}

					public void setRemarks(String remarks) {
						this.remarks = remarks;
					}

					public BubbleInfoBean getBubble_info() {
						return bubble_info;
					}

					public void setBubble_info(BubbleInfoBean bubble_info) {
						this.bubble_info = bubble_info;
					}

					public List<?> getAggregated_activity_codes() {
						return aggregated_activity_codes;
					}

					public void setAggregated_activity_codes(List<?> aggregated_activity_codes) {
						this.aggregated_activity_codes = aggregated_activity_codes;
					}

					public static class BubbleInfoBean {

						private boolean is_show;
						private int bubble_version;

						public boolean isIs_show() {
							return is_show;
						}

						public void setIs_show(boolean is_show) {
							this.is_show = is_show;
						}

						public int getBubble_version() {
							return bubble_version;
						}

						public void setBubble_version(int bubble_version) {
							this.bubble_version = bubble_version;
						}

						@Override
						public String toString() {
							return "BubbleInfoBean{" +
									"is_show=" + is_show +
									", bubble_version=" + bubble_version +
									'}';
						}
					}

					public boolean isChcked() {
						return isChcked;
					}

					public void setChcked(boolean chcked) {
						isChcked = chcked;
					}

					@Override
					public String toString() {
						return "ItemsBean{" +
								"code=" + code +
								", name='" + name + '\'' +
								", icon='" + icon + '\'' +
								", remarks='" + remarks + '\'' +
								", bubble_info=" + bubble_info +
								", aggregated_activity_codes=" + aggregated_activity_codes +
								", isChcked=" + isChcked +
								'}';
					}
				}
			}
		}
	}

	public static class IovBean {
	}
}
