package com.baidu.iov.dueros.waimai.net.entity.response;

import java.util.List;

public class FilterConditionResponse {

	/**
	 * meituan : {"code":0,"msg":"调用成功","errorInfo":null,
	 * "data":{"category_filter_list":[{"code":0,"name":"全部品类","quantity":3665,
	 * "sub_category_list":[]},{"code":910,"name":"美食","quantity":46,
	 * "sub_category_list":[{"code":0,"name":"全部","quantity":46,"icon_url":"http://p0.meituan
	 * .net/jungle/7448ec45e26edb34d327121ad7bfdb2111426.png"},{"code":100839,"name":"快餐便当",
	 * "quantity":16,"icon_url":"http://p0.meituan
	 * .net/jungle/12a331ee952b227b871490b7acd860f913252.png"},{"code":100841,"name":"地方菜系",
	 * "quantity":6,"icon_url":"http://p1.meituan
	 * .net/jungle/627aa3cb850eb6c8c1631933804f13b110938.png"},{"code":100944,"name":"特色小吃",
	 * "quantity":4,"icon_url":"http://p1.meituan
	 * .net/jungle/831b99f5a2642c10fabb5f638feef7e915759.png"},{"code":100845,"name":"香锅干锅",
	 * "quantity":3,"icon_url":"http://p1.meituan
	 * .net/jungle/7e4f57b155c11773302c13ceb55eb63f14704.png"},{"code":101789,"name":"嗨吃火锅",
	 * "quantity":4,"icon_url":"http://p1.meituan
	 * .net/jungle/f09a63d346afb9f4a78a4a487350217013220.png"},{"code":100844,"name":"龙虾烧烤",
	 * "quantity":3,"icon_url":"http://p1.meituan
	 * .net/jungle/75d32dc82c2083fe61297a040f380d4611210.png"}]},{"code":960,"name":"美团专送",
	 * "quantity":3625,"sub_category_list":[{"code":0,"name":"全部","quantity":3625,
	 * "icon_url":"http://p1.meituan.net/jungle/aa408d1920ce0165a94018b27897388314800.png"},
	 * {"code":100874,"name":"美食专送","quantity":28,"icon_url":"http://p0.meituan
	 * .net/jungle/6c7f11e1b050faac1c9a6804ee141aee7231.png"},{"code":100926,"name":"甜品饮品",
	 * "quantity":3597,"icon_url":"http://p1.meituan
	 * .net/jungle/d0c5aadd877f986688651ca9ef9abf7212196.png"}]},{"code":19,"name":"甜点饮品",
	 * "quantity":3612,"sub_category_list":[{"code":0,"name":"全部","quantity":3612,
	 * "icon_url":"http://p1.meituan.net/jungle/65cb45fe1f58b44e1dc24fe1e95341258004.png"},
	 * {"code":1042,"name":"可口甜品","quantity":3609,"icon_url":"http://p0.meituan
	 * .net/jungle/1b7e48257cea6114e051973eb346f63815593.png"},{"code":100837,"name":"面包烘焙",
	 * "quantity":3602,"icon_url":"http://p1.meituan
	 * .net/jungle/910b5a74b28b5a193dc6b4d547c51c5e11839.png"}]},{"code":100209,"name":"家常菜",
	 * "quantity":26,"sub_category_list":[{"code":0,"name":"全部","quantity":26,
	 * "icon_url":"http://p0.meituan.net/jungle/fb08b463cf54d3d0d5e80dc265bd750516668.png"},
	 * {"code":100857,"name":"北方佳肴","quantity":4,"icon_url":"http://p0.meituan
	 * .net/jungle/2671b31ebb347791b291e4c51013cf1113397.png"},{"code":100858,"name":"特色私房",
	 * "quantity":4,"icon_url":"http://p0.meituan
	 * .net/jungle/923b034947d0dbd8a171eb4d4e91ec0315163.png"}]},{"code":100180,"name":"小吃馆",
	 * "quantity":5,"sub_category_list":[{"code":0,"name":"全部","quantity":5,
	 * "icon_url":"http://p0.meituan.net/jungle/69735240114d8c7b5474677397d59c1b17953.png"},
	 * {"code":100240,"name":"麻辣烫","quantity":4,"icon_url":"http://p0.meituan
	 * .net/jungle/3dffede4faf110cad598091c55d825c017283.png"}]},{"code":100325,"name":"快食简餐",
	 * "quantity":16,"sub_category_list":[{"code":0,"name":"全部","quantity":16,
	 * "icon_url":"http://p1.meituan.net/jungle/04004d8ac0241e281f924ce22f03d3ff12635.png"},
	 * {"code":100966,"name":"快食盖饭","quantity":15,"icon_url":"http://p0.meituan
	 * .net/jungle/072c421a572c9c6522470babd1a13c7417688.png"}]},{"code":23,"name":"鲜花蛋糕",
	 * "quantity":3609,"sub_category_list":[{"code":0,"name":"全部","quantity":3609,
	 * "icon_url":"http://p1.meituan.net/jungle/630311b1bc660eed26202ac001f57cfd16808.png"},
	 * {"code":1063,"name":"浪漫鲜花","quantity":7,"icon_url":"http://p0.meituan
	 * .net/jungle/d32e435b0f2b519cb89fde29192eece716863.png"}]}],"sort_type_list":[{"code":0,
	 * "name":"综合排序","short_name":"","icon_url":"http://p1.meituan
	 * .net/aichequan/dfca522fc05590e55739d21c616bd271917.png","icon_url_click":"http://p1.meituan
	 * .net/aichequan/9b1bcc22831eaf93aea435a473522d70865.png","position":1},{"code":1,
	 * "name":"销量最高","short_name":"销量","icon_url":"http://p1.meituan
	 * .net/xianfu/51635adbb08819d5586c3e1feb0946c62052.png","icon_url_click":"http://p0.meituan
	 * .net/xianfu/31c8e28240571276801a6d99c55a88f82048.png","position":0},{"code":2,
	 * "name":"速度最快","short_name":"速度","icon_url":"http://p0.meituan
	 * .net/aichequan/30f5dccdf0131bc95b38c98d834d5ddd937.png","icon_url_click":"http://p0.meituan
	 * .net/aichequan/bdfa69ff79d35874e0db6cfd5554f626912.png","position":1},{"code":5,
	 * "name":"距离最近","short_name":"距离","icon_url":"http://p1.meituan
	 * .net/xianfu/a2b46aa4d74f015188a169de865ff0a62048.png","icon_url_click":"http://p1.meituan
	 * .net/xianfu/d5537fd9e6087644eca3588dba04f20f2048.png","position":0},{"code":3,
	 * "name":"评分最高","short_name":"评分","icon_url":"http://p1.meituan
	 * .net/aichequan/002a09315a10330efa4c185e1597a7ae1209.png",
	 * "icon_url_click":"http://p0.meituan
	 * .net/aichequan/0f5e1ded2dad17e4fe76f07da6c390e71192.png","position":1},{"code":4,
	 * "name":"起送价最低","short_name":"起送价","icon_url":"http://p1.meituan
	 * .net/aichequan/0c553181fbec8cb0d417ac5853d500eb1639.png",
	 * "icon_url_click":"http://p1.meituan
	 * .net/aichequan/6a02c53443e4bddd0c00358d32203a7d1535.png","position":1},{"code":6,
	 * "name":"配送费最低","short_name":"配送费","icon_url":"http://p0.meituan
	 * .net/aichequan/1b3acb36e6d7e48943d3546e47fdbbd61188.png",
	 * "icon_url_click":"http://p0.meituan
	 * .net/aichequan/0ffc191ed3ed6a5564196c67a5a083bc1093.png","position":1},{"code":8,
	 * "name":"人均高到低","short_name":"","icon_url":"http://p1.meituan
	 * .net/xianfu/72fd97c200c870ee8bf5d800dec4b25e1517.jpg","icon_url_click":"http://p0.meituan
	 * .net/aichequan/769a166c1c095ec8a3c47bdb70d4bd49353.png","position":1},{"code":7,
	 * "name":"人均低到高","short_name":"","icon_url":"http://p0.meituan
	 * .net/xianfu/f445e20e92645b4265b40ba5e04243a71485.jpg","icon_url_click":"http://p1.meituan
	 * .net/aichequan/248b54fa8a0b7b45f36365f2d96736c0351.png","position":1}],
	 * "activity_filter_list":[{"group_title":"","support_multi_choice":1,"items":[{"code":"-7",
	 * "name":"美团专送","icon":"http://p0.meituan
	 * .net/jungle/0c61c293ed906c70ef4d0e041b187ace1280.png","remarks":"",
	 * "aggregated_activity_codes":[],"bubble_info":{"is_show":false,"bubble_version":0}}],
	 * "display_style":0},{"group_title":"商家特色(可多选)","support_multi_choice":1,
	 * "items":[{"code":"-3","name":"免配送费","icon":"","remarks":"","aggregated_activity_codes":[],
	 * "bubble_info":{"is_show":false,"bubble_version":0}},{"code":"-4","name":"0元起送","icon":"",
	 * "remarks":"","aggregated_activity_codes":[],"bubble_info":{"is_show":false,
	 * "bubble_version":0}},{"code":"-8","name":"新商家","icon":"","remarks":"",
	 * "aggregated_activity_codes":[],"bubble_info":{"is_show":false,"bubble_version":0}},
	 * {"code":"-9","name":"品牌商家","icon":"","remarks":"","aggregated_activity_codes":[],
	 * "bubble_info":{"is_show":false,"bubble_version":0}},{"code":"511","name":"跨天预订","icon":"",
	 * "remarks":"","aggregated_activity_codes":[],"bubble_info":{"is_show":false,
	 * "bubble_version":0}},{"code":"-5","name":"支持开发票","icon":"","remarks":"",
	 * "aggregated_activity_codes":[],"bubble_info":{"is_show":false,"bubble_version":0}}],
	 * "display_style":0},{"group_title":"优惠活动(单选)","support_multi_choice":0,
	 * "items":[{"code":"1109","name":"进店领券","icon":"http://p0.meituan
	 * .net/xianfu/c2c0f31d0ebf0f60af115d058169c492992.png","remarks":"",
	 * "aggregated_activity_codes":[],"bubble_info":{"is_show":false,"bubble_version":0}},
	 * {"code":"1014","name":"满返代金券","icon":"http://p0.meituan
	 * .net/xianfu/652eea4034250563fe11b02e3219ba8d981.png","remarks":"",
	 * "aggregated_activity_codes":[],"bubble_info":{"is_show":false,"bubble_version":0}}],
	 * "display_style":1}]}}
	 * iov : {}
	 */

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
		/**
		 * code : 0
		 * msg : 调用成功
		 * errorInfo : null
		 * data : {"category_filter_list":[{"code":0,"name":"全部品类","quantity":3665,
		 * "sub_category_list":[]},{"code":910,"name":"美食","quantity":46,
		 * "sub_category_list":[{"code":0,"name":"全部","quantity":46,"icon_url":"http://p0.meituan
		 * .net/jungle/7448ec45e26edb34d327121ad7bfdb2111426.png"},{"code":100839,"name":"快餐便当",
		 * "quantity":16,"icon_url":"http://p0.meituan
		 * .net/jungle/12a331ee952b227b871490b7acd860f913252.png"},{"code":100841,"name":"地方菜系",
		 * "quantity":6,"icon_url":"http://p1.meituan
		 * .net/jungle/627aa3cb850eb6c8c1631933804f13b110938.png"},{"code":100944,"name":"特色小吃",
		 * "quantity":4,"icon_url":"http://p1.meituan
		 * .net/jungle/831b99f5a2642c10fabb5f638feef7e915759.png"},{"code":100845,"name":"香锅干锅",
		 * "quantity":3,"icon_url":"http://p1.meituan
		 * .net/jungle/7e4f57b155c11773302c13ceb55eb63f14704.png"},{"code":101789,"name":"嗨吃火锅",
		 * "quantity":4,"icon_url":"http://p1.meituan
		 * .net/jungle/f09a63d346afb9f4a78a4a487350217013220.png"},{"code":100844,"name":"龙虾烧烤",
		 * "quantity":3,"icon_url":"http://p1.meituan
		 * .net/jungle/75d32dc82c2083fe61297a040f380d4611210.png"}]},{"code":960,"name":"美团专送",
		 * "quantity":3625,"sub_category_list":[{"code":0,"name":"全部","quantity":3625,
		 * "icon_url":"http://p1.meituan.net/jungle/aa408d1920ce0165a94018b27897388314800.png"},
		 * {"code":100874,"name":"美食专送","quantity":28,"icon_url":"http://p0.meituan
		 * .net/jungle/6c7f11e1b050faac1c9a6804ee141aee7231.png"},{"code":100926,"name":"甜品饮品",
		 * "quantity":3597,"icon_url":"http://p1.meituan
		 * .net/jungle/d0c5aadd877f986688651ca9ef9abf7212196.png"}]},{"code":19,"name":"甜点饮品",
		 * "quantity":3612,"sub_category_list":[{"code":0,"name":"全部","quantity":3612,
		 * "icon_url":"http://p1.meituan.net/jungle/65cb45fe1f58b44e1dc24fe1e95341258004.png"},
		 * {"code":1042,"name":"可口甜品","quantity":3609,"icon_url":"http://p0.meituan
		 * .net/jungle/1b7e48257cea6114e051973eb346f63815593.png"},{"code":100837,"name":"面包烘焙",
		 * "quantity":3602,"icon_url":"http://p1.meituan
		 * .net/jungle/910b5a74b28b5a193dc6b4d547c51c5e11839.png"}]},{"code":100209,"name":"家常菜",
		 * "quantity":26,"sub_category_list":[{"code":0,"name":"全部","quantity":26,
		 * "icon_url":"http://p0.meituan.net/jungle/fb08b463cf54d3d0d5e80dc265bd750516668.png"},
		 * {"code":100857,"name":"北方佳肴","quantity":4,"icon_url":"http://p0.meituan
		 * .net/jungle/2671b31ebb347791b291e4c51013cf1113397.png"},{"code":100858,"name":"特色私房",
		 * "quantity":4,"icon_url":"http://p0.meituan
		 * .net/jungle/923b034947d0dbd8a171eb4d4e91ec0315163.png"}]},{"code":100180,"name":"小吃馆",
		 * "quantity":5,"sub_category_list":[{"code":0,"name":"全部","quantity":5,
		 * "icon_url":"http://p0.meituan.net/jungle/69735240114d8c7b5474677397d59c1b17953.png"},
		 * {"code":100240,"name":"麻辣烫","quantity":4,"icon_url":"http://p0.meituan
		 * .net/jungle/3dffede4faf110cad598091c55d825c017283.png"}]},{"code":100325,"name":"快食简餐",
		 * "quantity":16,"sub_category_list":[{"code":0,"name":"全部","quantity":16,
		 * "icon_url":"http://p1.meituan.net/jungle/04004d8ac0241e281f924ce22f03d3ff12635.png"},
		 * {"code":100966,"name":"快食盖饭","quantity":15,"icon_url":"http://p0.meituan
		 * .net/jungle/072c421a572c9c6522470babd1a13c7417688.png"}]},{"code":23,"name":"鲜花蛋糕",
		 * "quantity":3609,"sub_category_list":[{"code":0,"name":"全部","quantity":3609,
		 * "icon_url":"http://p1.meituan.net/jungle/630311b1bc660eed26202ac001f57cfd16808.png"},
		 * {"code":1063,"name":"浪漫鲜花","quantity":7,"icon_url":"http://p0.meituan
		 * .net/jungle/d32e435b0f2b519cb89fde29192eece716863.png"}]}],"sort_type_list":[{"code":0,
		 * "name":"综合排序","short_name":"","icon_url":"http://p1.meituan
		 * .net/aichequan/dfca522fc05590e55739d21c616bd271917.png",
		 * "icon_url_click":"http://p1.meituan
		 * .net/aichequan/9b1bcc22831eaf93aea435a473522d70865.png","position":1},{"code":1,
		 * "name":"销量最高","short_name":"销量","icon_url":"http://p1.meituan
		 * .net/xianfu/51635adbb08819d5586c3e1feb0946c62052.png",
		 * "icon_url_click":"http://p0.meituan
		 * .net/xianfu/31c8e28240571276801a6d99c55a88f82048.png","position":0},{"code":2,
		 * "name":"速度最快","short_name":"速度","icon_url":"http://p0.meituan
		 * .net/aichequan/30f5dccdf0131bc95b38c98d834d5ddd937.png",
		 * "icon_url_click":"http://p0.meituan
		 * .net/aichequan/bdfa69ff79d35874e0db6cfd5554f626912.png","position":1},{"code":5,
		 * "name":"距离最近","short_name":"距离","icon_url":"http://p1.meituan
		 * .net/xianfu/a2b46aa4d74f015188a169de865ff0a62048.png",
		 * "icon_url_click":"http://p1.meituan
		 * .net/xianfu/d5537fd9e6087644eca3588dba04f20f2048.png","position":0},{"code":3,
		 * "name":"评分最高","short_name":"评分","icon_url":"http://p1.meituan
		 * .net/aichequan/002a09315a10330efa4c185e1597a7ae1209.png",
		 * "icon_url_click":"http://p0.meituan
		 * .net/aichequan/0f5e1ded2dad17e4fe76f07da6c390e71192.png","position":1},{"code":4,
		 * "name":"起送价最低","short_name":"起送价","icon_url":"http://p1.meituan
		 * .net/aichequan/0c553181fbec8cb0d417ac5853d500eb1639.png",
		 * "icon_url_click":"http://p1.meituan
		 * .net/aichequan/6a02c53443e4bddd0c00358d32203a7d1535.png","position":1},{"code":6,
		 * "name":"配送费最低","short_name":"配送费","icon_url":"http://p0.meituan
		 * .net/aichequan/1b3acb36e6d7e48943d3546e47fdbbd61188.png",
		 * "icon_url_click":"http://p0.meituan
		 * .net/aichequan/0ffc191ed3ed6a5564196c67a5a083bc1093.png","position":1},{"code":8,
		 * "name":"人均高到低","short_name":"","icon_url":"http://p1.meituan
		 * .net/xianfu/72fd97c200c870ee8bf5d800dec4b25e1517.jpg",
		 * "icon_url_click":"http://p0.meituan
		 * .net/aichequan/769a166c1c095ec8a3c47bdb70d4bd49353.png","position":1},{"code":7,
		 * "name":"人均低到高","short_name":"","icon_url":"http://p0.meituan
		 * .net/xianfu/f445e20e92645b4265b40ba5e04243a71485.jpg",
		 * "icon_url_click":"http://p1.meituan
		 * .net/aichequan/248b54fa8a0b7b45f36365f2d96736c0351.png","position":1}],
		 * "activity_filter_list":[{"group_title":"","support_multi_choice":1,
		 * "items":[{"code":"-7","name":"美团专送","icon":"http://p0.meituan
		 * .net/jungle/0c61c293ed906c70ef4d0e041b187ace1280.png","remarks":"",
		 * "aggregated_activity_codes":[],"bubble_info":{"is_show":false,"bubble_version":0}}],
		 * "display_style":0},{"group_title":"商家特色(可多选)","support_multi_choice":1,
		 * "items":[{"code":"-3","name":"免配送费","icon":"","remarks":"",
		 * "aggregated_activity_codes":[],"bubble_info":{"is_show":false,"bubble_version":0}},
		 * {"code":"-4","name":"0元起送","icon":"","remarks":"","aggregated_activity_codes":[],
		 * "bubble_info":{"is_show":false,"bubble_version":0}},{"code":"-8","name":"新商家",
		 * "icon":"","remarks":"","aggregated_activity_codes":[],"bubble_info":{"is_show":false,
		 * "bubble_version":0}},{"code":"-9","name":"品牌商家","icon":"","remarks":"",
		 * "aggregated_activity_codes":[],"bubble_info":{"is_show":false,"bubble_version":0}},
		 * {"code":"511","name":"跨天预订","icon":"","remarks":"","aggregated_activity_codes":[],
		 * "bubble_info":{"is_show":false,"bubble_version":0}},{"code":"-5","name":"支持开发票",
		 * "icon":"","remarks":"","aggregated_activity_codes":[],"bubble_info":{"is_show":false,
		 * "bubble_version":0}}],"display_style":0},{"group_title":"优惠活动(单选)",
		 * "support_multi_choice":0,"items":[{"code":"1109","name":"进店领券",
		 * "icon":"http://p0.meituan.net/xianfu/c2c0f31d0ebf0f60af115d058169c492992.png",
		 * "remarks":"","aggregated_activity_codes":[],"bubble_info":{"is_show":false,
		 * "bubble_version":0}},{"code":"1014","name":"满返代金券","icon":"http://p0.meituan
		 * .net/xianfu/652eea4034250563fe11b02e3219ba8d981.png","remarks":"",
		 * "aggregated_activity_codes":[],"bubble_info":{"is_show":false,"bubble_version":0}}],
		 * "display_style":1}]}
		 */

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

			public static class CategoryFilterListBean {
				/**
				 * code : 0
				 * name : 全部品类
				 * quantity : 3665
				 * sub_category_list : []
				 */

				private long code;
				private String name;
				private int quantity;
				private List<?> sub_category_list;

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

				public List<?> getSub_category_list() {
					return sub_category_list;
				}

				public void setSub_category_list(List<?> sub_category_list) {
					this.sub_category_list = sub_category_list;
				}
			}

			public static class SortTypeListBean {
				/**
				 * code : 0
				 * name : 综合排序
				 * short_name :
				 * icon_url : http://p1.meituan
				 * .net/aichequan/dfca522fc05590e55739d21c616bd271917.png
				 * icon_url_click : http://p1.meituan
				 * .net/aichequan/9b1bcc22831eaf93aea435a473522d70865.png
				 * position : 1
				 */

				private long code;
				private String name;
				private String short_name;
				private String icon_url;
				private String icon_url_click;
				private int position;

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
			}

			public static class ActivityFilterListBean {
				/**
				 * group_title :
				 * support_multi_choice : 1
				 * items : [{"code":"-7","name":"美团专送","icon":"http://p0.meituan
				 * .net/jungle/0c61c293ed906c70ef4d0e041b187ace1280.png","remarks":"",
				 * "aggregated_activity_codes":[],"bubble_info":{"is_show":false,
				 * "bubble_version":0}}]
				 * display_style : 0
				 */

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

				public static class ItemsBean {
					/**
					 * code : -7
					 * name : 美团专送
					 * icon : http://p0.meituan.net/jungle/0c61c293ed906c70ef4d0e041b187ace1280.png
					 * remarks :
					 * aggregated_activity_codes : []
					 * bubble_info : {"is_show":false,"bubble_version":0}
					 */

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
						/**
						 * is_show : false
						 * bubble_version : 0
						 */

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
					}

					public boolean isChcked() {
						return isChcked;
					}

					public void setChcked(boolean chcked) {
						isChcked = chcked;
					}
				}
			}
		}
	}

	public static class IovBean {
	}
}
