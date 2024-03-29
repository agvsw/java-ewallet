package com.finalproject.walletforex.dao.implement;

import com.finalproject.walletforex.dao.AccountDao;
import com.finalproject.walletforex.dao.KursDao;
import com.finalproject.walletforex.dao.TransactionDao;
import com.finalproject.walletforex.dto.TransactionDto;
import com.finalproject.walletforex.exception.AccountNotFoundException;
import com.finalproject.walletforex.exception.BalanceNotEnoughException;
import com.finalproject.walletforex.model.Account;
import com.finalproject.walletforex.model.Kurs;
import com.finalproject.walletforex.model.Transaction;
import com.finalproject.walletforex.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class TransactionDaoImpl implements TransactionDao {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private KursDao kursDao;

    @Override
    public Transaction transaction(TransactionDto dto) throws AccountNotFoundException, BalanceNotEnoughException {
        Transaction transaction = setTransaction(dto);
        Account accountDebet    = accountDao.findById(transaction.getAccDebet());
        Account accountCredit   = accountDao.findById(transaction.getAccCredit());
        if (accountDebet.getCurencyType().equals(accountCredit.getCurencyType())){
            accountDebet.setBalance(accountDebet.getBalance() - transaction.getAmount());
            if (accountDebet.getBalance() < 0){
                throw new BalanceNotEnoughException(3, "Balance not enough");
            }
            accountCredit.setBalance(accountCredit.getBalance() + transaction.getAmount());
            accountDao.updateBalance(accountCredit);
            accountDao.updateBalance(accountDebet);
            return transactionRepository.save(transaction);

        } else {
            double result;
            Kurs kurs = kursDao.findByCcy(accountDebet.getCurencyType(), accountCredit.getCurencyType());
            if (kurs == null){
                kurs = kursDao.findByCcy(accountCredit.getCurencyType(), accountDebet.getCurencyType());
                result = kurs.getBuy() * transaction.getAmount();
            }else {
                result = transaction.getAmount() / kurs.getBuy();
            }
            accountDebet.setBalance(accountDebet.getBalance() - transaction.getAmount());
            if (accountDebet.getBalance() < 0){
                throw new BalanceNotEnoughException(3, "Balance not enough");
            }
            accountCredit.setBalance(accountCredit.getBalance() + result );
            accountDao.updateBalance(accountCredit);
            accountDao.updateBalance(accountDebet);
            return transactionRepository.save(transaction);
        }
    }

    @Override
    public List<Transaction> getList(String accNumber) throws AccountNotFoundException {
        List<Transaction> transaction = transactionRepository.findByAccCreditOrAccDebet(accNumber, accNumber);
        if (transaction.isEmpty()){
            throw new AccountNotFoundException(2,"Account have't Transaction");
        }
        return transaction;
    }

    private Transaction setTransaction(TransactionDto dto){
        Transaction transaction = new Transaction();
        transaction.setAccCredit(dto.getAccCredit());
        transaction.setAccDebet(dto.getAccDebet());
        transaction.setAmount(dto.getAmount());
        transaction.setDate((Date) dto.getDate());
        transaction.setTransactionType(dto.getTransactionType());
        return transaction;
    }
}
