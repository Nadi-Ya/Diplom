package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.HomePage;
import page.CreditPage;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditTest {
    HomePage home;
    CreditPage creditPage;
    String approvedCardNumber = DataHelper.getCardApproved().getCardNumber();
    String declinedCardNumber = DataHelper.getCardDeclined().getCardNumber();
    String randomCardNumber = DataHelper.getRandomCardNumber();
    String validMonth = DataHelper.getRandomMonth(1);
    String validYear = DataHelper.getRandomYear(1);
    String validOwnerName = DataHelper.getRandomName();
    String validCode = DataHelper.getNumberCVC(3);


    @BeforeEach
    public void setUp() {
        open("http://localhost:8080");
        home = new HomePage();
        creditPage = home.creditPayment();
    }


    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterAll
    public static void shouldCleanBase() {
        SQLHelper.cleanBase();
    }

    @Test
    public void shouldApprovedCreditPayment() {
        var creditPage = home.creditPayment();
        creditPage.cleanFields();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditPage.bankApprovedOperation();
        assertEquals("APPROVED", SQLHelper.getCreditPayment());
    }



    @Test
    public void shouldDeclinedCreditPayment() {
        var creditPage = home.creditPayment();
        creditPage.cleanFields();
        creditPage.fillCardPaymentForm(declinedCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditPage.bankDeclinedOperation();
        assertEquals("DECLINED", SQLHelper.getCreditPayment());
    }

    @Test
    public void shouldRandomCardNumberCreditPayment() {
        var creditPage = home.creditPayment();
        creditPage.cleanFields();
        creditPage.fillCardPaymentForm(randomCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditPage.bankDeclinedOperation();
    }

    @Test
    public void shouldInvalidCardNumberCreditPayment() {
        var creditPage = home.creditPayment();
        var invalidCardNumber = DataHelper.getRandomShorterCardNumber();
        creditPage.cleanFields();
        creditPage.fillCardPaymentForm(invalidCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditPage.errorFormat();
    }

    @Test
    public void shouldEmptyCardNumberCreditPayment() {
        var emptyCardNumber = DataHelper.getEmptyField();
        creditPage.cleanFields();
        creditPage.fillCardPaymentForm(emptyCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditPage.errorFormat();
    }

    @Test
    public void shouldCreditPaymentWithMonthTermValidityExpired() {
        var creditPage = home.creditPayment();
        var currentYear = DataHelper.getRandomYear(0);
        var monthTermValidityExpired = DataHelper.getRandomMonth(-1);
        creditPage.cleanFields();
        creditPage.fillCardPaymentForm(approvedCardNumber, monthTermValidityExpired, currentYear, validOwnerName, validCode);
        creditPage.errorCardTermValidity();
    }

    @Test
    public void shouldCardPaymentWithInvalidMonth() {
        var creditPage = home.creditPayment();
        var invalidMonth = DataHelper.getInvalidMonth();
        creditPage.cleanFields();
        creditPage.fillCardPaymentForm(approvedCardNumber, invalidMonth, validYear, validOwnerName, validCode);
        creditPage.errorCardTermValidity();
    }

    @Test
    public void shouldCardPaymentWithEmptyMonth() {
        var emptyMonth = DataHelper.getEmptyField();
        creditPage.cleanFields();
        creditPage.fillCardPaymentForm(approvedCardNumber, emptyMonth, validYear, validOwnerName, validCode);
        creditPage.errorFormat();
    }

    @Test
    public void shouldCreditPaymentWithYearTermValidityExpired() {
        var creditPage = home.creditPayment();
        var yearTermValidityExpired = DataHelper.getRandomYear(-1);
        creditPage.cleanFields();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, yearTermValidityExpired, validOwnerName, validCode);
        creditPage.termValidityExpired();
    }

    @Test
    public void shouldCreditPaymentWithInvalidYear() {
        var creditPage = home.creditPayment();
        var invalidYear = DataHelper.getRandomYear(6);
        creditPage.cleanFields();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, invalidYear, validOwnerName, validCode);
        creditPage.errorCardTermValidity();
    }

    @Test
    public void shouldCreditPaymentWithEmptyYear() {
        var creditPage = home.creditPayment();
        var emptyYear = DataHelper.getEmptyField();
        creditPage.cleanFields();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, emptyYear, validOwnerName, validCode);
        creditPage.errorFormat();
    }

    @Test
    public void shouldRusLangNameCreditPayment() {
        var creditPage = home.creditPayment();
        var rusLangName = DataHelper.getRandomNameRus();
        creditPage.cleanFields();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, rusLangName, validCode);
        creditPage.errorFormat();
    }

    @Test
    public void shouldDigitsNameCreditPayment() {
        var creditPage = home.creditPayment();
        var digitsName = DataHelper.getNumberName();
        creditPage.cleanFields();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, digitsName, validCode);
        creditPage.errorFormat();
    }

    @Test
    public void shouldSpecSymbolsNameCreditPayment() {
        var creditPage = home.creditPayment();
        var specSymbolsName = DataHelper.getSpecSymbolName();
        creditPage.cleanFields();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, specSymbolsName, validCode);
        creditPage.errorFormat();
    }

    @Test
    public void shouldEmptyNameCreditPayment() {
        var creditPage = home.creditPayment();
        var emptyName = DataHelper.getEmptyField();
        creditPage.cleanFields();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, emptyName, validCode);
        creditPage.emptyField();
    }

    @Test
    public void shouldTwoDigitsCodeCreditPayment() {
        var creditPage = home.creditPayment();
        var twoDigitsCode = DataHelper.getNumberCVC(2);
        creditPage.cleanFields();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, twoDigitsCode);
        creditPage.errorFormat();
    }

    @Test
    public void shouldLettersCodeCreditPayment() {
        var creditPage = home.creditPayment();
        var lettersCode = DataHelper.getRandomName();
        creditPage.cleanFields();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, lettersCode);
        creditPage.errorFormat();
    }

    @Test
    public void shouldSpecSymbolsCodeCreditPayment() {
        var creditPage = home.creditPayment();
        var specSymbolsCode = DataHelper.getSpecSymbolName();
        creditPage.cleanFields();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, specSymbolsCode);
        creditPage.errorFormat();
    }

    @Test
    public void shouldEmptyCodeCreditPayment() {
        var creditPage = home.creditPayment();
        var emptyCode = DataHelper.getEmptyField();
        creditPage.cleanFields();
        creditPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, emptyCode);
        creditPage.errorFormat();
    }
}
