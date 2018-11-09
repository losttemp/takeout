package com.baidu.iov.dueros.waimai.net.entity.response;

import java.util.List;

public class StoreResponse {

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

	public static class MeituanBean {

		private int code;
		private String msg;
		private ErrorInfoBean errorInfo;
		private DataBean data;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public ErrorInfoBean getErrorInfo() {
			return errorInfo;
		}

		public void setErrorInfo(ErrorInfoBean errorInfo) {
			this.errorInfo = errorInfo;
		}

		public DataBean getData() {
			return data;
		}

		public void setData(DataBean data) {
			this.data = data;
		}

		public static class ErrorInfoBean {

			private String failCode;
			private String name;
			private String description;

			public String getFailCode() {
				return failCode;
			}

			public void setFailCode(String failCode) {
				this.failCode = failCode;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getDescription() {
				return description;
			}

			public void setDescription(String description) {
				this.description = description;
			}
		}

		public static class DataBean {

			private int poi_total_num;
			private int have_next_page;
			private int current_page_index;
			private int page_size;
			private List<OpenPoiBaseInfoListBean> openPoiBaseInfoList;

			public int getPoi_total_num() {
				return poi_total_num;
			}

			public void setPoi_total_num(int poi_total_num) {
				this.poi_total_num = poi_total_num;
			}

			public int getHave_next_page() {
				return have_next_page;
			}

			public void setHave_next_page(int have_next_page) {
				this.have_next_page = have_next_page;
			}

			public int getCurrent_page_index() {
				return current_page_index;
			}

			public void setCurrent_page_index(int current_page_index) {
				this.current_page_index = current_page_index;
			}

			public int getPage_size() {
				return page_size;
			}

			public void setPage_size(int page_size) {
				this.page_size = page_size;
			}

			public List<OpenPoiBaseInfoListBean> getOpenPoiBaseInfoList() {
				return openPoiBaseInfoList;
			}

			public void setOpenPoiBaseInfoList(List<OpenPoiBaseInfoListBean> openPoiBaseInfoList) {
				this.openPoiBaseInfoList = openPoiBaseInfoList;
			}

			public static class OpenPoiBaseInfoListBean {

				private long wm_poi_id;
				private int status;
				private String status_desc;
				private String name;
				private String pic_url;
				private double shipping_fee;
				private double min_price;
				private double wm_poi_score;
				private int avg_delivery_time;
				private String poi_type_icon;
				private String distance;
				private int latitude;
				private int longitude;
				private String address;
				private int month_sale_num;
				private int delivery_type;
				private int invoice_support;
				private int invoice_min_price;
				private String average_price_tip;
				private List<ProductListBean> product_list;
				private List<DiscountsBean> discounts;
				private boolean isDiscountsDown;

				public long getWm_poi_id() {
					return wm_poi_id;
				}

				public void setWm_poi_id(long wm_poi_id) {
					this.wm_poi_id = wm_poi_id;
				}

				public int getStatus() {
					return status;
				}

				public void setStatus(int status) {
					this.status = status;
				}

				public String getStatus_desc() {
					return status_desc;
				}

				public void setStatus_desc(String status_desc) {
					this.status_desc = status_desc;
				}

				public String getName() {
					return name;
				}

				public void setName(String name) {
					this.name = name;
				}

				public String getPic_url() {
					return pic_url;
				}

				public void setPic_url(String pic_url) {
					this.pic_url = pic_url;
				}

				public double getShipping_fee() {
					return shipping_fee;
				}

				public void setShipping_fee(double shipping_fee) {
					this.shipping_fee = shipping_fee;
				}

				public double getMin_price() {
					return min_price;
				}

				public void setMin_price(double min_price) {
					this.min_price = min_price;
				}

				public double getWm_poi_score() {
					return wm_poi_score;
				}

				public void setWm_poi_score(double wm_poi_score) {
					this.wm_poi_score = wm_poi_score;
				}

				public int getAvg_delivery_time() {
					return avg_delivery_time;
				}

				public void setAvg_delivery_time(int avg_delivery_time) {
					this.avg_delivery_time = avg_delivery_time;
				}

				public String getPoi_type_icon() {
					return poi_type_icon;
				}

				public void setPoi_type_icon(String poi_type_icon) {
					this.poi_type_icon = poi_type_icon;
				}

				public String getDistance() {
					return distance;
				}

				public void setDistance(String distance) {
					this.distance = distance;
				}

				public int getLatitude() {
					return latitude;
				}

				public void setLatitude(int latitude) {
					this.latitude = latitude;
				}

				public int getLongitude() {
					return longitude;
				}

				public void setLongitude(int longitude) {
					this.longitude = longitude;
				}

				public String getAddress() {
					return address;
				}

				public void setAddress(String address) {
					this.address = address;
				}

				public int getMonth_sale_num() {
					return month_sale_num;
				}

				public void setMonth_sale_num(int month_sale_num) {
					this.month_sale_num = month_sale_num;
				}

				public int getDelivery_type() {
					return delivery_type;
				}

				public void setDelivery_type(int delivery_type) {
					this.delivery_type = delivery_type;
				}

				public int getInvoice_support() {
					return invoice_support;
				}

				public void setInvoice_support(int invoice_support) {
					this.invoice_support = invoice_support;
				}

				public int getInvoice_min_price() {
					return invoice_min_price;
				}

				public void setInvoice_min_price(int invoice_min_price) {
					this.invoice_min_price = invoice_min_price;
				}

				public String getAverage_price_tip() {
					return average_price_tip;
				}

				public void setAverage_price_tip(String average_price_tip) {
					this.average_price_tip = average_price_tip;
				}

				public List<ProductListBean> getProduct_list() {
					return product_list;
				}

				public void setProduct_list(List<ProductListBean> product_list) {
					this.product_list = product_list;
				}

				public List<DiscountsBean> getDiscounts() {
					return discounts;
				}

				public void setDiscounts(List<DiscountsBean> discounts) {
					this.discounts = discounts;
				}

				public boolean isDiscountsDown() {
					return isDiscountsDown;
				}

				public void setDiscountsDown(boolean discountsDown) {
					isDiscountsDown = discountsDown;
				}

				public static class ProductListBean {

					private int id;
					private String name;
					private double price;
					private String picture;

					public int getId() {
						return id;
					}

					public void setId(int id) {
						this.id = id;
					}

					public String getName() {
						return name;
					}

					public void setName(String name) {
						this.name = name;
					}

					public double getPrice() {
						return price;
					}

					public void setPrice(double price) {
						this.price = price;
					}

					public String getPicture() {
						return picture;
					}

					public void setPicture(String picture) {
						this.picture = picture;
					}
				}

				public static class DiscountsBean {

					private String info;
					private String icon_url;

					public String getInfo() {
						return info;
					}

					public void setInfo(String info) {
						this.info = info;
					}

					public String getIcon_url() {
						return icon_url;
					}

					public void setIcon_url(String icon_url) {
						this.icon_url = icon_url;
					}
				}
			}
		}
	}

	public static class IovBean {
	}
}
