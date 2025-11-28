package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.ConstantTestValue.*;
import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト 勤怠管理機能
 * ケース12
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース12 受講生 勤怠直接編集 入力チェック")
public class Case12 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		goTo(LMS_LOGIN_URL);
		// 各画面表示時に10秒待機する
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "transitionLogin_1");
		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_LOGIN_URL);
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		//要素取得
		WebElement loginId = webDriver.findElement(By.name(HTML_NAME_LOGINID));
		WebElement password = webDriver.findElement(By.name(HTML_NAME_PASS));
		WebElement loginButton = webDriver.findElement(By.xpath(HTML_XPATH_INPUT_VALUE_LOGIN));

		//要素入力
		loginId.sendKeys(EXIST_STUDENT_ID);
		password.sendKeys(EXIST_STUDENT_PASS);
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "beforeLogin_1");
		loginButton.click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterLogin_2");

		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_COURSE_DETAIL_URL);
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() {
		//上部メニューの「勤怠」リンクから勤怠管理画面に遷移
		webDriver.findElement(By.xpath(HTML_XPATH_A_TEXT_ATTEND)).click();
		Alert alert = webDriver.switchTo().alert();
		alert.accept();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterAttendance_1");
		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_ATTENDANCE_DETAIL_URL);
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「勤怠情報を直接編集する」リンクから勤怠情報直接変更画面に遷移")
	void test04() {
		//上部メニューの「勤怠」リンクから勤怠管理画面に遷移
		webDriver.findElement(By.xpath(HTML_XPATH_A_TEXT_ATTEND_EDIT)).click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterUpdateAttendance_1");
		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_ATTENDANCE_UPDATE_URL);
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 不適切な内容で修正してエラー表示：出退勤の（時）と（分）のいずれかが空白")
	void test05() {
		Select startHour = new Select(webDriver.findElement(By.id(HTML_ID_STARTHOUR_ZERO)));
		Select startMinute = new Select(webDriver.findElement(By.id(HTML_ID_STARTMINUTE_ZERO)));

		startHour.selectByVisibleText(NINE_HOUR_AM);
		startMinute.selectByVisibleText(BLANK);
		WebDriverUtils.scrollBy(ONE_THOUSAND_PIXEL);
		WebElement updateButton = webDriver.findElement(By.className(HTML_CLASSNAME_UPDATEBUTTON));
		updateButton.click();

		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();

		WebElement error = webDriver.findElement(By.className(HTML_CLASSNAME_HELPINLINE));
		assertEquals(error.getText(), "* 出勤時間が正しく入力されていません。");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正してエラー表示：出勤が空白で退勤に入力あり")
	void test06() {
		Select startHour = new Select(webDriver.findElement(By.id(HTML_ID_STARTHOUR_ZERO)));
		Select startMinute = new Select(webDriver.findElement(By.id(HTML_ID_STARTMINUTE_ZERO)));
		Select endHour = new Select(webDriver.findElement(By.id(HTML_ID_ENDHOUR_ZERO)));
		Select endMinute = new Select(webDriver.findElement(By.id(HTML_ID_ENDMINUTE_ZERO)));

		startHour.selectByVisibleText(BLANK);
		startMinute.selectByVisibleText(BLANK);
		endHour.selectByVisibleText(SIX_HOUR_PM);
		endMinute.selectByVisibleText(ZERO_MINUTE);
		WebDriverUtils.scrollBy(ONE_THOUSAND_PIXEL);
		WebElement updateButton = webDriver.findElement(By.className(HTML_CLASSNAME_UPDATEBUTTON));
		updateButton.click();
		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();

		WebElement error = webDriver.findElement(By.className(HTML_CLASSNAME_HELPINLINE));
		assertEquals(error.getText(), ERROR_MSG_CANT_INPUT_LEVING_WITHOUT_ATTEND);
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正してエラー表示：出勤が退勤よりも遅い時間")
	void test07() {
		Select startHour = new Select(webDriver.findElement(By.id(HTML_ID_STARTHOUR_ZERO)));
		Select startMinute = new Select(webDriver.findElement(By.id(HTML_ID_STARTMINUTE_ZERO)));
		Select endHour = new Select(webDriver.findElement(By.id(HTML_ID_ENDHOUR_ZERO)));
		Select endMinute = new Select(webDriver.findElement(By.id(HTML_ID_ENDMINUTE_ZERO)));

		startHour.selectByVisibleText(SEVEN_HOUR_PM);
		startMinute.selectByVisibleText(ZERO_MINUTE);
		endHour.selectByVisibleText(SIX_HOUR_PM);
		endMinute.selectByVisibleText(ZERO_MINUTE);
		WebDriverUtils.scrollBy(ONE_THOUSAND_PIXEL);
		WebElement updateButton = webDriver.findElement(By.className(HTML_CLASSNAME_UPDATEBUTTON));
		updateButton.click();
		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();

		WebElement error = webDriver.findElement(By.className(HTML_CLASSNAME_HELPINLINE));
		assertEquals(error.getText(), ERROR_MSG_CANT_INPUT_LEVING_FASTER_THAN_ATTEND);
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正してエラー表示：出退勤時間を超える中抜け時間")
	void test08() {
		Select startHour = new Select(webDriver.findElement(By.id(HTML_ID_STARTHOUR_ZERO)));
		Select startMinute = new Select(webDriver.findElement(By.id(HTML_ID_STARTMINUTE_ZERO)));
		Select endHour = new Select(webDriver.findElement(By.id(HTML_ID_ENDHOUR_ZERO)));
		Select endMinute = new Select(webDriver.findElement(By.id(HTML_ID_ENDMINUTE_ZERO)));
		Select blankTime = new Select(webDriver.findElement(By.name(HTML_NAME_BLANKTIME_ZERO)));

		startHour.selectByVisibleText(NINE_HOUR_AM);
		startMinute.selectByVisibleText(ZERO_MINUTE);
		endHour.selectByVisibleText(ONE_HOUR_PM);
		endMinute.selectByVisibleText(ZERO_MINUTE);
		blankTime.selectByVisibleText(SEVEN_HOURS_BLANKTIME);
		WebDriverUtils.scrollBy(ONE_THOUSAND_PIXEL);
		WebElement updateButton = webDriver.findElement(By.className(HTML_CLASSNAME_UPDATEBUTTON));
		updateButton.click();
		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();

		WebElement error = webDriver.findElement(By.className(HTML_CLASSNAME_HELPINLINE));
		assertEquals(error.getText(), ERROR_MSG_CANT_INPUT_BLANKTIME_MORE_THAN_WORKINGTIME);
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正してエラー表示：備考が100文字超")
	void test09() {
		Select startHour = new Select(webDriver.findElement(By.id(HTML_ID_STARTHOUR_ZERO)));
		Select startMinute = new Select(webDriver.findElement(By.id(HTML_ID_STARTMINUTE_ZERO)));
		Select endHour = new Select(webDriver.findElement(By.id(HTML_ID_ENDHOUR_ZERO)));
		Select endMinute = new Select(webDriver.findElement(By.id(HTML_ID_ENDMINUTE_ZERO)));
		Select blankTime = new Select(webDriver.findElement(By.name(HTML_NAME_BLANKTIME_ZERO)));
		WebElement note = webDriver.findElement(By.name(HTML_NAME_NOTE_ZERO));
		note.sendKeys(HUNDRED_WORD);
		startHour.selectByVisibleText(NINE_HOUR_AM);
		startMinute.selectByVisibleText(ZERO_MINUTE);
		endHour.selectByVisibleText(SIX_HOUR_PM);
		endMinute.selectByVisibleText(ZERO_MINUTE);
		blankTime.selectByVisibleText(BLANK);
		WebDriverUtils.scrollBy(ONE_THOUSAND_PIXEL);
		WebElement updateButton = webDriver.findElement(By.className(HTML_CLASSNAME_UPDATEBUTTON));
		updateButton.click();
		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();

		WebElement error = webDriver.findElement(By.className(HTML_CLASSNAME_HELPINLINE));
		assertEquals(error.getText(), ERROR_MSG_CANT_INPUT_NOTE_MORE_THAN_ONEHUNDRED_WORD);
	}

}
