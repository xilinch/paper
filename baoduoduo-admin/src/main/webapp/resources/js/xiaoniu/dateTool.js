var dateTools = {
		/**
		 * 格式化日期
		 */
		simpleFormatDate : function(date) {
			var year = date.getFullYear(),
				month = (date.getMonth() + 1),
				day = date.getDate();
			if(month < 10) {month = "0" + month;}
			if(day < 10) {day = "0" + day;}
			return year + '-' + month + "-" + day;	
		},
		LongTimeToDateString : function(longTime){
			var date = new Date(longTime);
			var year = date.getFullYear(),
			month = (date.getMonth() + 1),
			day = date.getDate();
			hours = date.getHours();
			minutes = date.getMinutes();
			seconds = date.getSeconds();
			if(month < 10) {month = "0" + month;}
			if(day < 10) {day = "0" + day;}
			if(hours < 10 ){ hours = "0" + hours;}
			if(minutes < 10){minutes = "0" + minutes;}
			if(seconds < 10){seconds ="0"+seconds;}
			return year + '-' + month + "-" + day + " " + hours + ":"+minutes+":"+seconds;
		},
		LongTimeToSimpleFormatDate : function(longTime){
			var date = new Date(longTime);
			var year = date.getFullYear(),
			month = (date.getMonth() + 1),
			day = date.getDate();
			if(month < 10) {month = "0" + month;}
			if(day < 10) {day = "0" + day;}
			return year + '-' + month + "-" + day;
		},
		getDay:function(longTime){
			var date = new Date(longTime);
			return date.getDate();
		},
		getYearAndMoth:function(longTime){
			var date = new Date(longTime);
			var year = date.getFullYear(),
			month = (date.getMonth() + 1);
			return year+','+month;
		}
		
};