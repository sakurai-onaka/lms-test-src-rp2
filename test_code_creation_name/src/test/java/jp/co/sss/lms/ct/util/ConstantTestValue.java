package jp.co.sss.lms.ct.util;

public class ConstantTestValue {
	public static final String LMS_LOGIN_URL = "http://localhost:8080/lms/";
	public static final String LMS_RE_LOGIN_URL = "http://localhost:8080/lms/login";
	public static final String LMS_COURSE_DETAIL_URL = "http://localhost:8080/lms/course/detail";
	public static final String LMS_USER_AGREESECURITY_URL = "http://localhost:8080/lms/user/agreeSecurity";
	public static final String LMS_PASSWORD_CHANGE_URL = "http://localhost:8080/lms/password/changePassword";
	public static final String LMS_ATTENDANCE_DETAIL_URL = "http://localhost:8080/lms/attendance/detail";
	public static final String LMS_ATTENDANCE_UPDATE_URL = "http://localhost:8080/lms/attendance/update";
	public static final String LMS_HELP_URL = "http://localhost:8080/lms/help";
	public static final String LMS_FAQ_URL = "http://localhost:8080/lms/faq";
	public static final String LMS_SECTION_DETAIL_URL = "http://localhost:8080/lms/section/detail";
	public static final String LMS_REPORT_REGIST_URL = "http://localhost:8080/lms/report/regist";
	public static final String LMS_LMS_USER_DETAIL_URL = "http://localhost:8080/lms/user/detail";
	public static final String LMS_LMS_EXAM_START_URL = "http://localhost:8080/lms/exam/start";
	public static final String LMS_LMS_EXAM_QUESTION_URL = "http://localhost:8080/lms/exam/question";
	public static final String LMS_LMS_EXAM_ANSWERCHECK_URL = "http://localhost:8080/lms/exam/answerCheck";
	public static final String LMS_LMS_EXAM_RESULT_URL = "http://localhost:8080/lms/exam/result";
	public static final Integer WAIT_FIVE_SECOND = 5;
	public static final Integer WAIT_TEN_SECOND = 10;

	public static final String HTML_TAGNAME_DIV = "div";
	public static final String HTML_TAGNAME_DT = "dt";
	public static final String HTML_TAGNAME_DL = "dl";
	public static final String HTML_TAGNAME_DD = "dd";
	public static final String HTML_TAGNAME_TEXTAREA = "textarea";
	public static final String HTML_NAME_LOGINID = "loginId";
	public static final String HTML_NAME_PASS = "password";
	public static final String HTML_NAME_KEYWORD = "password";
	public static final String HTML_XPATH_INPUT_VALUE_LOGIN = "//input[@value='ログイン']";
	public static final String HTML_XPATH_INPUT_VALUE_CONTAINS_SUBMITTED_DAILYREPO = ".//input[contains(@value,'提出済み日報')]";
	public static final String HTML_XPATH_CHILDELEM_INPUT_VALUE_DETALI = ".//input[@value='詳細']";
	public static final String HTML_XPATH_INPUT_VALUE_CONTAINS_DAILYREPO = "//input[contains(@value,'日報')]";
	public static final String HTML_XPATH_BUTTON_TEXT_CONTAINS_SUBMISSION = "//button[contains(text(),'提出する')]";
	
	public static final String HTML_XPATH_A_HREF_CONTAINS_FAQ_CATE_1 = "//a[contains(@href, 'frequentlyAskedQuestionCategoryId=1')]";
	public static final String HTML_XPATH_A_HREF_CONTAINS_FAQ_CATE_2 = "//a[contains(@href, 'frequentlyAskedQuestionCategoryId=2')]";
	public static final String HTML_XPATH_A_HREF_CONTAINS_FAQ_CATE_3 = "//a[contains(@href, 'frequentlyAskedQuestionCategoryId=3')]";
	public static final String HTML_XPATH_INPUT_VALUE_CLEAR = "//input[@value='クリア']";
	public static final String HTML_XPATH_A_TEXT_FAQ = "//a[text()='よくある質問']";
	public static final String HTML_CLASSNAME_DROPDOWN_TOGGLE = "dropdown-toggle";
	public static final String HTML_CLASSNAME_WELL = "well";

	
	public static final String DRIVER_TYPE = "webdriver.chrome.driver";
	public static final String ZERO_PIXEL = "0";
	public static final Integer SEPARATE_TAB = 1;
	public static final Integer CATEGORYID_1 = 1;
	public static final Integer CATEGORYID_2 = 2;
	public static final Integer CATEGORYID_3 = 3;
	public static final String DRIVER_EXE_PATH = "lib/chromedriver.exe";
	public static final String NO_EXIST_STUDENT = "noExistStudent";
	public static final String EXIST_STUDENT_ID = "StudentAA01";
	public static final String EXIST_STUDENT_PASS = "System3sss";
	public static final String FIRST_LOGIN_STUDENT_ID = "StudentAA05";
	public static final String ERROR_MSG_FAIL_LOGIN = "* ログインに失敗しました。";
	public static final String SEARCH_KEYWORD = "セルフ";
	public static final String NO_SUBMISSION = "未提出";
	public static final String SUBMITTED_DAILYREPO = "提出済み日報";
	public static final String DAILYREPO_CONTENT = "abcdef50983k";
	public static final String HUNDRED_WORD = "]dy*,t?#Upuy*@8%8!j[Qt2]ZIZxCR9'$Q1|j0f[z'\".e;u?a>sS.ihv[jl=E#vVaxqOK+k@o~:XgiqX|&T}'1?s]XHL:C|h2ZM*a";
	public static final String TWO_THOUSAND_WORD = "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";

}
