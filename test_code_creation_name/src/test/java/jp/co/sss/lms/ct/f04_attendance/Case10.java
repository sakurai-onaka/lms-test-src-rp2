package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.ConstantTestValue.*;
import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

/**
 * 結合テスト 勤怠管理機能
 * ケース10
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース10 受講生 勤怠登録 正常系")
public class Case10 {

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
		webDriver.findElement(By.xpath("//a[text()='勤怠']")).click();
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
	@DisplayName("テスト04 「出勤」ボタンを押下し出勤時間を登録")
	void test04() {
		webDriver.findElement(By.xpath(".//input[@value='出勤']")).click();
		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterAttendance_1");

		//本日の取得
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
		String todayString = today.format(formatter);
		//テストコード実行時の日付と合致する勤怠があるか確認
		WebElement now = webDriver.findElement(By.id("now"));
		WebElement targetRecord = null;
		List<WebElement> trElems = webDriver.findElements(By.xpath("//table/tbody/tr"));
		//打刻した日付の情報を取得
		for (WebElement trElem : trElems) {
			if (trElem.getText().contains(todayString)) {
				targetRecord = trElem;
				break;
			}
		}
		//打刻した日付の出勤情報を取得
		List<WebElement> targetTableData = targetRecord.findElements(By.xpath(".//td"));
		WebElement attendance = null;
		int count = 0;
		for (WebElement tdElem : targetTableData) {
			if (count == 2) {
				attendance = tdElem;
				break;
			}
			count++;
		}

		String attendanceTime = attendance.getText();
		String nowTime = now.getText();
		assertTrue(nowTime.contains(attendanceTime));
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「退勤」ボタンを押下し退勤時間を登録")
	void test05() {
		webDriver.findElement(By.xpath(".//input[@value='退勤']")).click();
		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterLeaving_1");

		//本日の取得
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
		String todayString = today.format(formatter);
		//テストコード実行時の日付と合致する勤怠があるか確認
		WebElement now = webDriver.findElement(By.id("now"));
		WebElement targetRecord = null;
		List<WebElement> trElems = webDriver.findElements(By.xpath("//table/tbody/tr"));
		//打刻した日付の情報を取得
		for (WebElement trElem : trElems) {
			if (trElem.getText().contains(todayString)) {
				targetRecord = trElem;
				break;
			}
		}
		//打刻した日付の出勤情報を取得
		List<WebElement> targetTableData = targetRecord.findElements(By.xpath(".//td"));
		WebElement leaving = null;
		int count = 0;
		for (WebElement tdElem : targetTableData) {
			if (count == 3) {
				leaving = tdElem;
				break;
			}
			count++;
		}

		String leavingTime = leaving.getText();
		String nowTime = now.getText();
		assertTrue(nowTime.contains(leavingTime));
	}

}
