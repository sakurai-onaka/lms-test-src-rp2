package jp.co.sss.lms.ct.f02_faq;

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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jp.co.sss.lms.ct.util.WebDriverUtils;
import jp.co.sss.lms.dto.FaqDto;
import jp.co.sss.lms.form.FaqSearchForm;
import jp.co.sss.lms.service.FaqService;

/**
 * 結合テスト よくある質問機能
 * ケース06
 * @author holy
 */
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

	@Autowired
	private FaqService faqService;

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
		setWaitTime();
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
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		//要素取得
		WebElement functionButton = webDriver.findElement(By.className(HTML_CLASSNAME_DROPDOWN_TOGGLE));
		functionButton.click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "beforeClickHelp_1");
		//プルダウンのヘルプを取得
		webDriver.findElement(By.xpath("//a[text()='ヘルプ']")).click();
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterClickHelp_2");
		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_HELP_URL);
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		//よくある質問を取得
		webDriver.findElement(By.xpath(HTML_XPATH_A_TEXT_FAQ)).click();
		//別タブに切り替え
		Object[] windowHandles = webDriver.getWindowHandles().toArray();
		webDriver.switchTo().window((String) windowHandles[SEPARATE_TAB]);
		//エビデンス取得・テスト
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "beforeClickFAQ_1");
		String nowURL = webDriver.getCurrentUrl();
		assertEquals(nowURL, LMS_FAQ_URL);
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {
		//【研修関係】を押下
		webDriver.findElement(By.xpath(HTML_XPATH_A_HREF_CONTAINS_FAQ_CATE_1)).click();
		//frequentlyAskedQuestionCategoryId=1での検索結果を取得
		checkCategoryIdTest(CATEGORYID_1);
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterClickSearch_1");
		
		//【人材開発支援助成金】を押下
		webDriver.findElement(By.xpath(HTML_XPATH_A_HREF_CONTAINS_FAQ_CATE_2)).click();
		//frequentlyAskedQuestionCategoryId=1での検索結果を取得
		checkCategoryIdTest(CATEGORYID_2);
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterClickSearch_2");

		//【遠隔研修】を押下
		webDriver.findElement(By.xpath(HTML_XPATH_A_HREF_CONTAINS_FAQ_CATE_3)).click();
		//frequentlyAskedQuestionCategoryId=1での検索結果を取得
		checkCategoryIdTest(CATEGORYID_3);
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterClickSearch_3");
		
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {
		//検索結果に検索した文字列が含まれるかチェック
		WebDriverUtils.scrollTo(ZERO_PIXEL);
		//【研修関係】を押下
		webDriver.findElement(By.xpath(HTML_XPATH_A_HREF_CONTAINS_FAQ_CATE_1)).click();
		WebDriverUtils.scrollByClassName(HTML_CLASSNAME_WELL);
		List<WebElement> resultList = webDriver.findElements(By.tagName(HTML_TAGNAME_DL));
		for (WebElement result : resultList) {
			result.click();
			int height = result.getSize().getHeight();
			WebDriverUtils.scrollBy(String.valueOf(height));
			//回答をクリックしたときの文字列が取得できるかチェック
			String text = result.findElement(By.tagName(HTML_TAGNAME_DD)).getText();
			assertNotNull(text);
		}
		webDriver.manage().timeouts().implicitlyWait(WAIT_TEN_SECOND, TimeUnit.SECONDS);
		getEvidence(new Object() {
		}, "afterClickSearchResults");
	}

	
	public void checkCategoryIdTest(Integer frequentlyAskedQuestionCategoryId) {
		FaqSearchForm faqSearchForm = new FaqSearchForm();
		faqSearchForm.setFrequentlyAskedQuestionCategoryId(frequentlyAskedQuestionCategoryId);
		List<FaqDto> searchResults = faqService.getFaqDtoList(faqSearchForm);
		List<WebElement> resultList = webDriver.findElements(By.tagName("dl"));
		//検索結果が該当カテゴリに合致するか確認
		for (FaqDto searchResult : searchResults) {
			WebDriverUtils.scrollTo(ZERO_PIXEL);
			WebDriverUtils.scrollByClassName(HTML_CLASSNAME_WELL);
			boolean matchFlg = false;
			//検索結果に一致するかチェック
			for (WebElement result : resultList) {
				if (searchResult.getQuestion().equals(result.getText().substring(2))) {
					matchFlg = true;
				}
				int height = result.getSize().getHeight();
				WebDriverUtils.scrollBy(String.valueOf(height));
			}
			assertTrue(matchFlg);
		}
	}
}
