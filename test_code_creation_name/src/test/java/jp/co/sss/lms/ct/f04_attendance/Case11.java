package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.ConstantTestValue.*;
import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

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
import org.openqa.selenium.support.ui.Select;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト 勤怠管理機能
 * ケース11
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース11 受講生 勤怠直接編集 正常系")
public class Case11 {

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
	@DisplayName("テスト04 「勤怠情報を直接編集する」リンクから勤怠情報直接変更画面に遷移")
	void test04() {
		//上部メニューの「勤怠」リンクから勤怠管理画面に遷移
		webDriver.findElement(By.xpath("//a[text()='勤怠情報を直接編集する']")).click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterUpdateAttendance_1");
		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_ATTENDANCE_UPDATE_URL);
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 すべての研修日程の勤怠情報を正しく更新し勤怠管理画面に遷移")
	void test05() {
		List<WebElement> attendRecords = webDriver.findElements(By.xpath("//tbody/tr"));
		for (int i = 0; i < attendRecords.size(); i++) {
			Select startHour = new Select(
					webDriver.findElement(By.name("attendanceList[" + i + "].trainingStartTimeHour")));
			Select startMinute = new Select(
					webDriver.findElement(By.name("attendanceList[" + i + "].trainingStartTimeMinute")));
			Select endHour = new Select(
					webDriver.findElement(By.name("attendanceList[" + i + "].trainingEndTimeHour")));
			Select endMinute = new Select(
					webDriver.findElement(By.name("attendanceList[" + i + "].trainingEndTimeMinute")));
			switch (i) {
			case 0:
				startHour.selectByVisibleText("09");
				startMinute.selectByVisibleText("00");
				endHour.selectByVisibleText("18");
				endMinute.selectByVisibleText("00");
				break;
			case 1:
				startHour.selectByVisibleText("11");
				startMinute.selectByVisibleText("00");
				endHour.selectByVisibleText("13");
				endMinute.selectByVisibleText("00");
				break;
			}
		}
		WebDriverUtils.scrollBy("1000");
		webDriver.findElement(By.xpath("//input[@value='更新']")).click();
		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();

		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterUpdateAttendance_1");
		
		List<WebElement> resultAttendRecords = webDriver.findElements(By.xpath("//tbody/tr"));
		String attendTime = null;
		String levingTime = null;
		for (int i = 0; i < resultAttendRecords.size(); i++) {
			List<WebElement> tdResultAttendRecords = resultAttendRecords.get(i).findElements(By.xpath(".//td"));
			//出退勤時間取得
			for (int j = 0; j < tdResultAttendRecords.size(); j++) {
				if (j == 2) {
					attendTime = tdResultAttendRecords.get(j).getText();
				} else if (j == 3) {
					levingTime = tdResultAttendRecords.get(j).getText();
				}
			}
			//出退勤時間テスト
			if (i == 0) {
				assertEquals(attendTime,"09:00");
				assertEquals(levingTime,"18:00");
			} else if (i == 1) {
				assertEquals(attendTime,"11:00");
				assertEquals(levingTime,"13:00");
			}
		}
	}
}
