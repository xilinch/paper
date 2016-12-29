var stringTool = {
		isEmail:function(str){
			var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
			return reg.test(str);
		},
		isPhone:function(str){
			var reg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
			return reg.test(str);
		},
		isCardNo:function(str){
			var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
			return reg.test(str);
		},
		/*
		isCardNo:function(cardid) {
		     //身份证正则表达式(18位) 
		     var isIdCard2 = /^[1-9]d{5}(19d{2}|[2-9]d{3})((0d)|(1[0-2]))(([0|1|2]d)|3[0-1])(d{4}|d{3}X)$/i;
		     var stard = "10X98765432"; //最后一位身份证的号码
		     var first = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2]; //1-17系数
		     var sum = 0;
		     if (!isIdCard2.test(cardid)) {
		         return false;
		     }
		     var year = cardid.substr(6, 4);
		     var month = cardid.substr(10, 2);
		     var day = cardid.substr(12, 2);
		     var birthday = cardid.substr(6, 8);
		     if (birthday != dateToString(new Date(year + '/' + month + '/' + day))) { //校验日期是否合法
		         return false;
		     }
		     for (var i = 0; i < cardid.length - 1; i++) {
		         sum += cardid[i] * first[i];
		     }
		     var result = sum % 11;
		     var last = stard[result]; //计算出来的最后一位身份证号码
		     if (cardid[cardid.length - 1].toUpperCase() == last) {
		         return true;
		     } else {
		         return false;
		     }
		 },
		 //日期转字符串 返回日期格式20080808
		dateToString:function(date) {
		     if (date instanceof Date) {
		         var year = date.getFullYear();
		         var month = date.getMonth() + 1;
		         month = month < 10 ? '0' + month: month;
		         var day = date.getDate();
		         day = day < 10 ? '0' + day: day;
		         return year + month + day;
		     }
		     return '';
		 }*/
};