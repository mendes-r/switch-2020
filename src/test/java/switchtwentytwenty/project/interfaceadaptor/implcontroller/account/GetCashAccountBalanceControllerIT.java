package switchtwentytwenty.project.interfaceadaptor.implcontroller.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import switchtwentytwenty.project.Application;
import switchtwentytwenty.project.applicationservice.appservice.iappservice.IFamilyAndMemberService;
import switchtwentytwenty.project.applicationservice.irepository.IAccountRepository;
import switchtwentytwenty.project.applicationservice.irepository.IFamilyRepository;
import switchtwentytwenty.project.applicationservice.irepository.IPersonRepository;
import switchtwentytwenty.project.domain.aggregate.account.Account;
import switchtwentytwenty.project.domain.aggregate.account.AccountFactory;
import switchtwentytwenty.project.domain.aggregate.family.Family;
import switchtwentytwenty.project.domain.aggregate.family.FamilyFactory;
import switchtwentytwenty.project.domain.aggregate.person.Person;
import switchtwentytwenty.project.domain.aggregate.person.PersonFactory;
import switchtwentytwenty.project.domain.constant.Constants;
import switchtwentytwenty.project.domain.share.MoneyValue;
import switchtwentytwenty.project.domain.share.designation.AccountDesignation;
import switchtwentytwenty.project.domain.share.familydata.FamilyName;
import switchtwentytwenty.project.domain.share.id.AccountID;
import switchtwentytwenty.project.domain.share.id.Email;
import switchtwentytwenty.project.domain.share.id.FamilyID;
import switchtwentytwenty.project.domain.share.id.LedgerID;
import switchtwentytwenty.project.domain.share.persondata.*;
import switchtwentytwenty.project.domain.share.persondata.address.Address;
import switchtwentytwenty.project.dto.outdto.CashAccountBalanceOutDTO;
import switchtwentytwenty.project.dto.todomaindto.FamilyVoDTO;
import switchtwentytwenty.project.dto.todomaindto.PersonVoDTO;
import switchtwentytwenty.project.interfaceadaptor.icontroller.account.IGetCashAccountBalanceController;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Application.class)
public class GetCashAccountBalanceControllerIT {


    @Autowired
    IAccountRepository accountRepository;
    @Autowired
    IFamilyRepository familyRepository;
    @Autowired
    IPersonRepository personRepository;
    @Autowired
    IFamilyAndMemberService familyAndMemberService;
    @Autowired
    IGetCashAccountBalanceController getCashAccountBalanceController;

    @Test
    @DisplayName("Get Cash account balance - successful case")
    void getCashAccountBalance() throws Exception {

        //arrange
        double initialAmount = 50;
        String designation = "Cash";

        //Create family
        String adminId = "jonh@gmail.com";
        UUID familyUUID = UUID.randomUUID();
        FamilyID familyID = new FamilyID(familyUUID);
        FamilyVoDTO familyDTO = new FamilyVoDTO(familyID, new LedgerID(UUID.randomUUID()), new Email(adminId), new FamilyName("Santos"));
        Family family = FamilyFactory.create(familyDTO);
        familyRepository.save(family);

        //Add Admin to the repository
        TelephoneNumberList list = new TelephoneNumberList();
        list.add(new TelephoneNumber("228888541"));
        Email adminID = new Email(adminId);
        Address address = new Address("street", "25", "4125-886", "Porto", "Portugal");
        BirthDate birthDate = new BirthDate("1968-01-22");
        PersonName name = new PersonName("John");
        VAT vat = new VAT("123456789");
        PersonVoDTO personVoDTO = new PersonVoDTO(name, birthDate, vat, address, list, adminID, familyID, new LedgerID(UUID.randomUUID()));
        Person admin = PersonFactory.create(personVoDTO);
        personRepository.save(admin);

        MoneyValue initialAmountValue = new MoneyValue(new BigDecimal(initialAmount));
        AccountDesignation initialAccountDesignation = new AccountDesignation(designation);
        AccountID accountID = new AccountID(UUID.randomUUID());
        Account cashAccount = AccountFactory.createCashAccount(accountID, initialAccountDesignation, initialAmountValue);
        admin.addAccountID(accountID);
        personRepository.save(admin);
        accountRepository.save(cashAccount);

        CashAccountBalanceOutDTO expectedDto = new CashAccountBalanceOutDTO(50);
        ResponseEntity<Object> expected = new ResponseEntity<>(expectedDto, HttpStatus.OK);

        //act
        ResponseEntity<Object> result = getCashAccountBalanceController.getCashAccountBalance(adminID.toString(), accountID.toString());
        int statusCodeExpected = 200;

        //assert
        assertEquals(statusCodeExpected, result.getStatusCodeValue());
        assertEquals(expected,result);
    }

    @Test
    @DisplayName("Get Family Cash account balance - successful case")
    void getFamilyCashAccountBalance() throws Exception {

        //arrange
        double initialAmount = 50;
        String designation = "Cash";

        //Create family
        String adminId = "jonh@gmail.com";
        UUID familyUUID = UUID.randomUUID();
        FamilyID familyID = new FamilyID(familyUUID);
        FamilyVoDTO familyDTO = new FamilyVoDTO(familyID, new LedgerID(UUID.randomUUID()), new Email(adminId), new FamilyName("Santos"));
        Family family = FamilyFactory.create(familyDTO);
        familyRepository.save(family);

        //Add Admin to the repository
        TelephoneNumberList list = new TelephoneNumberList();
        list.add(new TelephoneNumber("228888541"));
        Email adminID = new Email(adminId);
        Address address = new Address("street", "25", "4125-886", "Porto", "Portugal");
        BirthDate birthDate = new BirthDate("1968-01-22");
        PersonName name = new PersonName("John");
        VAT vat = new VAT("123456789");
        PersonVoDTO personVoDTO = new PersonVoDTO(name, birthDate, vat, address, list, adminID, familyID, new LedgerID(UUID.randomUUID()));
        Person admin = PersonFactory.create(personVoDTO);
        personRepository.save(admin);

        //Create Family Cash Account
        MoneyValue initialAmountValue = new MoneyValue(new BigDecimal(initialAmount));
        AccountDesignation initialAccountDesignation = new AccountDesignation(designation);
        AccountID accountID = new AccountID(UUID.randomUUID());
        Account cashAccount = AccountFactory.createCashAccount(accountID, initialAccountDesignation, initialAmountValue);
        family.addAccountID(accountID);
        familyRepository.save(family);
        accountRepository.save(cashAccount);

        CashAccountBalanceOutDTO expectedDto = new CashAccountBalanceOutDTO(50);
        ResponseEntity<Object> expected = new ResponseEntity<>(expectedDto, HttpStatus.OK);

        //act
        ResponseEntity<Object> result = getCashAccountBalanceController.getCashAccountBalance(adminID.toString(), accountID.toString());
        int statusCodeExpected = 200;

        //assert
        assertEquals(statusCodeExpected, result.getStatusCodeValue());
        assertEquals(expected,result);
    }

    @Test
    @DisplayName("Get Family members Cash account balance - successful case")
    void getFamilyMembersCashAccountBalance() throws Exception {

        //arrange
        double initialAmount = 50;
        String designation = "Cash";

        //Create family
        String adminId = "jonh@gmail.com";
        UUID familyUUID = UUID.randomUUID();
        FamilyID familyID = new FamilyID(familyUUID);
        FamilyVoDTO familyDTO = new FamilyVoDTO(familyID, new LedgerID(UUID.randomUUID()), new Email(adminId), new FamilyName("Santos"));
        Family family = FamilyFactory.create(familyDTO);
        familyRepository.save(family);

        //Add Admin to the repository
        TelephoneNumberList list = new TelephoneNumberList();
        list.add(new TelephoneNumber("228888541"));
        Email adminID = new Email(adminId);
        Address address = new Address("street", "25", "4125-886", "Porto", "Portugal");
        BirthDate birthDate = new BirthDate("1968-01-22");
        PersonName name = new PersonName("John");
        VAT vat = new VAT("123456789");
        PersonVoDTO personVoDTO = new PersonVoDTO(name, birthDate, vat, address, list, adminID, familyID, new LedgerID(UUID.randomUUID()));
        Person admin = PersonFactory.create(personVoDTO);
        personRepository.save(admin);

        //Add Family Member
        Email mattID = new Email("matt@gmail.com");
        PersonName mattName = new PersonName("Matt");
        VAT mattVat = new VAT("232098018");
        PersonVoDTO memberVoDTO = new PersonVoDTO(mattName, birthDate, mattVat, address, list, mattID, familyID, new LedgerID(UUID.randomUUID()));
        Person matt = PersonFactory.create(personVoDTO);
        personRepository.save(matt);


        //Create Matt Cash Account
        MoneyValue initialAmountValue = new MoneyValue(new BigDecimal(initialAmount));
        AccountDesignation initialAccountDesignation = new AccountDesignation(designation);
        AccountID accountID = new AccountID(UUID.randomUUID());
        Account cashAccount = AccountFactory.createCashAccount(accountID, initialAccountDesignation, initialAmountValue);
        matt.addAccountID(accountID);
         personRepository.save(matt);
        accountRepository.save(cashAccount);

        CashAccountBalanceOutDTO expectedDto = new CashAccountBalanceOutDTO(50);
        ResponseEntity<Object> expected = new ResponseEntity<>(expectedDto, HttpStatus.OK);

        //act
        ResponseEntity<Object> result = getCashAccountBalanceController.getCashAccountBalance(adminID.toString(), accountID.toString());
        int statusCodeExpected = 200;

        //assert
        assertEquals(statusCodeExpected, result.getStatusCodeValue());
        assertEquals(expected,result);
    }


    @Test
    @DisplayName("Get non-Cash account balance - unsuccessful case")
    void getNonCashAccountBalance() throws Exception {

        //arrange
        String designation = "bank";
        //Create family
        String adminId = "jonh@gmail.com";
        UUID familyUUID = UUID.randomUUID();
        FamilyID familyID = new FamilyID(familyUUID);
        FamilyVoDTO familyDTO = new FamilyVoDTO(familyID, new LedgerID(UUID.randomUUID()), new Email(adminId), new FamilyName("Santos"));
        Family family = FamilyFactory.create(familyDTO);
        familyRepository.save(family);

        //Add Admin to the repository
        TelephoneNumberList list = new TelephoneNumberList();
        list.add(new TelephoneNumber("228888541"));
        Email adminID = new Email(adminId);
        Address address = new Address("street", "25", "4125-886", "Porto", "Portugal");
        BirthDate birthDate = new BirthDate("1968-01-22");
        PersonName name = new PersonName("John");
        VAT vat = new VAT("123456789");
        PersonVoDTO personVoDTO = new PersonVoDTO(name, birthDate, vat, address, list, adminID, familyID, new LedgerID(UUID.randomUUID()));
        Person admin = PersonFactory.create(personVoDTO);
        personRepository.save(admin);

        AccountDesignation accountDesignation = new AccountDesignation(designation);
        AccountID accountID = new AccountID(UUID.randomUUID());

        Account bankSavingsAccount = AccountFactory.createBankAccount(accountID, accountDesignation, Constants.BANK_SAVINGS_ACCOUNT_TYPE);
        admin.addAccountID(accountID);
        this.accountRepository.save(bankSavingsAccount);
        this.personRepository.save(admin);


        //act
        ResponseEntity<Object> result = getCashAccountBalanceController.getCashAccountBalance(adminID.toString(), accountID.toString());
        int statusCodeExpected = 400;

        //assert
        assertEquals(statusCodeExpected, result.getStatusCodeValue());

    }

    @Test
    @DisplayName("Get Cash account balance when i am not the admin - unsuccessful case")
    void getCashAccountBalanceWhenIAmNotTheAdmin() throws Exception {

        //arrange
        double initialAmount = 50;
        String designation = "Cash";

        //Create family
        String adminId = "matt@gmail.com";
        UUID familyUUID = UUID.randomUUID();
        FamilyID familyID = new FamilyID(familyUUID);
        FamilyVoDTO familyDTO = new FamilyVoDTO(familyID, new LedgerID(UUID.randomUUID()), new Email(adminId), new FamilyName("Santos"));
        Family family = FamilyFactory.create(familyDTO);
        familyRepository.save(family);

        //Add Admin to the repository
        TelephoneNumberList list = new TelephoneNumberList();
        list.add(new TelephoneNumber("228888541"));
        Email adminID = new Email("john@gmail.com");
        Address address = new Address("street", "25", "4125-886", "Porto", "Portugal");
        BirthDate birthDate = new BirthDate("1968-01-22");
        PersonName name = new PersonName("John");
        VAT vat = new VAT("123456789");
        PersonVoDTO personVoDTO = new PersonVoDTO(name, birthDate, vat, address, list, adminID, familyID, new LedgerID(UUID.randomUUID()));
        Person admin = PersonFactory.create(personVoDTO);
        personRepository.save(admin);

        MoneyValue initialAmountValue = new MoneyValue(new BigDecimal(initialAmount));
        AccountDesignation initialAccountDesignation = new AccountDesignation(designation);
        AccountID accountID = new AccountID(UUID.randomUUID());
        Account cashAccount = AccountFactory.createCashAccount(accountID, initialAccountDesignation, initialAmountValue);
        admin.addAccountID(accountID);
        personRepository.save(admin);
        accountRepository.save(cashAccount);



        //act
        ResponseEntity<Object> result = getCashAccountBalanceController.getCashAccountBalance(adminID.toString(), accountID.toString());
        int statusCodeExpected = 400;

        //assert
        assertEquals(statusCodeExpected, result.getStatusCodeValue());

    }













}
