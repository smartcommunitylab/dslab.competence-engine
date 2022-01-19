package it.smartcommunitylab.scoengine.model.esco;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EscoResponse {
		private String total;
		private String offset;
		private String limit;
		
//		@JsonIgnore		
//		private String text;
//		@JsonIgnore
//		private String language;
//		@JsonIgnore
//		private String[] type;
//		@JsonIgnore
//		private boolean isInScheme;
//		@JsonIgnore
//		private boolean facet;
//		@JsonIgnore
//		private Object links;
//		@JsonIgnore
//		private Object _embedded;
		
		public String getTotal() {
			return total;
		}
		public void setTotal(String total) {
			this.total = total;
		}
		public String getOffset() {
			return offset;
		}
		public void setOffset(String offset) {
			this.offset = offset;
		}
		public String getLimit() {
			return limit;
		}
		public void setLimit(String limit) {
			this.limit = limit;
		}
		
		
}
