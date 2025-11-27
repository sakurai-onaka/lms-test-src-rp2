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
		WebElement loginId = webDriver.findElement(By.name("loginId"));
		WebElement password = webDriver.findElement(By.name("password"));
		WebElement loginButton = webDriver.findElement(By.xpath("//input[@value='ログイン']"));

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
	@DisplayName("テスト05 不適切な内容で修正してエラー表示：出退勤の（時）と（分）のいずれかが空白")
	void test05() {
		Select startHour = new Select(webDriver.findElement(By.id("startHour0")));
		Select startMinute = new Select(webDriver.findElement(By.id("startMinute0")));

		startHour.selectByVisibleText("09");
		startMinute.selectByVisibleText("");
		WebDriverUtils.scrollBy("1000");
		WebElement updateButton = webDriver.findElement(By.className("update-button"));
		updateButton.click();
		
		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();
		
		WebElement error = webDriver.findElement(By.className("help-inline"));
		assertEquals(error.getText(),"* 出勤時間が正しく入力されていません。");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正してエラー表示：出勤が空白で退勤に入力あり")
	void test06() {
		Select startHour = new Select(webDriver.findElement(By.id("startHour0")));
		Select startMinute = new Select(webDriver.findElement(By.id("startMinute0")));
		Select endHour = new Select(webDriver.findElement(By.id("endHour0")));
		Select endMinute = new Select(webDriver.findElement(By.id("endMinute0")));
		
		startHour.selectByVisibleText("");
		startMinute.selectByVisibleText("");
		endHour.selectByVisibleText("18");
		endMinute.selectByVisibleText("00");
		WebDriverUtils.scrollBy("1000");
		WebElement updateButton = webDriver.findElement(By.className("update-button"));
		updateButton.click();
		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();
		
		WebElement error = webDriver.findElement(By.className("help-inline"));
		assertEquals(error.getText(),"* 出勤情報がないため退勤情報を入力出来ません。");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正してエラー表示：出勤が退勤よりも遅い時間")
	void test07() {
		Select startHour = new Select(webDriver.findElement(By.id("startHour0")));
		Select startMinute = new Select(webDriver.findElement(By.id("startMinute0")));
		Select endHour = new Select(webDriver.findElement(By.id("endHour0")));
		Select endMinute = new Select(webDriver.findElement(By.id("endMinute0")));
		
		startHour.selectByVisibleText("19");
		startMinute.selectByVisibleText("00");
		endHour.selectByVisibleText("18");
		endMinute.selectByVisibleText("00");
		WebDriverUtils.scrollBy("1000");
		WebElement updateButton = webDriver.findElement(By.className("update-button"));
		updateButton.click();
		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();
		
		WebElement error = webDriver.findElement(By.className("help-inline"));
		assertEquals(error.getText(),"* 退勤時刻[0]は出勤時刻[0]より後でなければいけません。");
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正してエラー表示：出退勤時間を超える中抜け時間")
	void test08() {
		Select startHour = new Select(webDriver.findElement(By.id("startHour0")));
		Select startMinute = new Select(webDriver.findElement(By.id("startMinute0")));
		Select endHour = new Select(webDriver.findElement(By.id("endHour0")));
		Select endMinute = new Select(webDriver.findElement(By.id("endMinute0")));
		Select blankTime = new Select(webDriver.findElement(By.name("attendanceList[0].blankTime")));
		
		startHour.selectByVisibleText("09");
		startMinute.selectByVisibleText("00");
		endHour.selectByVisibleText("12");
		endMinute.selectByVisibleText("00");
		blankTime.selectByVisibleText("7時間");
		WebDriverUtils.scrollBy("1000");
		WebElement updateButton = webDriver.findElement(By.className("update-button"));
		updateButton.click();
		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();
		
		WebElement error = webDriver.findElement(By.className("help-inline"));
		assertEquals(error.getText(),"* 中抜け時間が勤務時間を超えています。");
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正してエラー表示：備考が100文字超")
	void test09() {
		Select startHour = new Select(webDriver.findElement(By.id("startHour0")));
		Select startMinute = new Select(webDriver.findElement(By.id("startMinute0")));
		Select endHour = new Select(webDriver.findElement(By.id("endHour0")));
		Select endMinute = new Select(webDriver.findElement(By.id("endMinute0")));
		Select blankTime = new Select(webDriver.findElement(By.name("attendanceList[0].blankTime")));
		WebElement note = webDriver.findElement(By.name("attendanceList[0].note"));
		note.sendKeys("]dy*,t?#Upuy*@8%8!j[Qt2]ZIZxCR9'$Q1|j0f[z'\".e;u?a>sS.ihv[jl=E#vVaxqOK+k@o~:XgiqX|&T}'1?s]XHL:C|h2ZM*a");
		startHour.selectByVisibleText("09");
		startMinute.selectByVisibleText("00");
		endHour.selectByVisibleText("18");
		endMinute.selectByVisibleText("00");
		blankTime.selectByVisibleText("");
		WebDriverUtils.scrollBy("1000");
		WebElement updateButton = webDriver.findElement(By.className("update-button"));
		updateButton.click();
		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();
		
		WebElement error = webDriver.findElement(By.className("help-inline"));
		assertEquals(error.getText(),"* 備考の長さが最大値(100)を超えています。");
	}

}
