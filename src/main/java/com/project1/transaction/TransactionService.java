package com.project1.transaction;

import com.project1.auditing.ApplicationAuditAware;
import com.project1.offer.OfferRepository;
import com.project1.offer.data.Offer;
import com.project1.project.ProjectRepository;
import com.project1.project.data.Project;
import com.project1.project.data.ProjectStatus;
import com.project1.projectProgress.ProjectProgressRepository;
import com.project1.transaction.data.AcceptOfferRequest;
import com.project1.transaction.data.AdminTransactionDTO;
import com.project1.transaction.data.Transaction;
import com.project1.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.project1.transaction.data.TransactionDTO;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.google.cloud.Timestamp.now;
import static com.project1.transaction.TransactionType.HOLD;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final TransactionMapper transactionMapper;
    private final ApplicationAuditAware applicationAuditAware;
    private final OfferRepository offerRepository;
    private final ProjectRepository projectRepository;
    public List<Transaction> getTransactionsBySenderId(Long senderId) {
        return transactionRepository.findBySenderUserId(senderId);
    }

    public List<Transaction> getTransactionsByReceiverId(Long receiverId) {
        return transactionRepository.findByReceiverUserId(receiverId);
    }
    public List<Transaction> getTransactionsByReceiverOrSender(Long userId) {
        return transactionRepository.findByReceiverUserIdOrSenderUserId(userId, userId);
    }

    public List<Transaction> getTransactionsByType(TransactionType type) {
        return transactionRepository.findByType(type);
    }
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow();
    }


    public Transaction createAdminTransaction(AdminTransactionDTO admintransactiondto) {
        Transaction transaction = transactionMapper.toEntity(admintransactiondto);
        long transactionNumber;
        do {
            Random random = new Random();
            transactionNumber = random.nextInt(900000000) + 100000000L;
        }while(transactionRepository.findByTransactionNumber(transactionNumber)!=null);
        transaction.setTransactionNumber(transactionNumber);
        Integer currentAuditor = applicationAuditAware.getCurrentAuditor().orElseThrow();
        transaction.setSenderUserId(currentAuditor.longValue());
        switch (transaction.getType()) {
            case DEPOSIT:
                return depositTransaction(transaction);
            case WITHDRAW:
                return withdrawTransaction(transaction);
            default:
                throw new UnsupportedOperationException("Unsupported transaction type: " + transaction.getType());
        }
    }

    public Transaction createTransaction(TransactionDTO transactiondto) {
        Transaction transaction = transactionMapper.toEntity(transactiondto);
        long transactionNumber;
        do {
            Random random = new Random();
            transactionNumber = random.nextInt(900000000) + 100000000L;
        }while(transactionRepository.findByTransactionNumber(transactionNumber)!=null);
        transaction.setTransactionNumber(transactionNumber);
        switch (transaction.getType()) {
            case HOLD:
                return holdTransaction(transaction);
            case TRANSFER:
                return transferTransaction(transaction);
            case TRANSFERHELD:
                return transferHeldTransaction(transaction);
            case UNHOLD:
                return unholdTransaction(transaction);
            default:
                throw new UnsupportedOperationException("Unsupported transaction type: " + transaction.getType());
        }
    }



    public Transaction AcceptOfferTransaction(Long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found with id " + offerId));

        Integer currentAuditorId = applicationAuditAware.getCurrentAuditor()
                .orElseThrow(() -> new RuntimeException("Current auditor not found"));

       Project project = offer.getProject();

//       if(project.getStatus()!= ProjectStatus.open)
//       {
//           throw new RuntimeException("Project is not open for offers");
//
//       }

//        System.out.println(Long.valueOf(currentAuditorId));
//        System.out.println(Long.valueOf(project.getClient().getUser().getId()));

        if (!project.getClient().getUser().getId().equals(currentAuditorId)) {
            throw new RuntimeException("You are not authorized to accept this offer");
        }


        Transaction transaction = Transaction.builder()
                .receiverUserId(Long.valueOf(project.getClient().getUser().getId()))
                .transactionDate(new Date())
                .amount(offer.getCost() * 0.2)
                .type(HOLD)
                .build();

        return createTransaction(transactionMapper.toDto(transaction));
    }


    public Transaction finishedProjectTransaction(Long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found with id " + offerId));

        Integer currentAuditorId = applicationAuditAware.getCurrentAuditor()
                .orElseThrow(() -> new RuntimeException("Current auditor not found"));

        Project project = offer.getProject();

//       if(project.getStatus()!= ProjectStatus.open)
//       {
//           throw new RuntimeException("Project is not open for offers");
//
//       }

//        System.out.println(Long.valueOf(currentAuditorId));
//        System.out.println(Long.valueOf(project.getClient().getUser().getId()));

        if (!project.getClient().getId().equals(Long.valueOf(currentAuditorId))) {
            throw new RuntimeException("You are not authorized to accept this offer");
        }


        Transaction transaction = Transaction.builder()
                .senderUserId(Long.valueOf(project.getClient().getUser().getId()))
                .receiverUserId(Long.valueOf(offer.getWorker().getUser().getId()))
                .transactionDate(new Date())
                .amount(offer.getCost() * 0.8)
                .type(TransactionType.TRANSFER)
                .build();

        Transaction transaction2 = Transaction.builder()
                .senderUserId(Long.valueOf(project.getClient().getUser().getId()))
                .receiverUserId(Long.valueOf(offer.getWorker().getUser().getId()))
                .transactionDate(new Date())
                .amount(offer.getCost() * 0.2)
                .type(TransactionType.TRANSFERHELD)
                .build();
        createTransaction(transactionMapper.toDto(transaction2));
        return createTransaction(transactionMapper.toDto(transaction)) ;
    }



    private Transaction depositTransaction(Transaction transaction) {
        walletService.addTotalBalance(transaction.getReceiverUserId(), transaction.getAmount());
        return transactionRepository.save(transaction);
    }

    private Transaction withdrawTransaction(Transaction transaction) {
        walletService.subtractTotalBalance(transaction.getReceiverUserId(), transaction.getAmount());
        return transactionRepository.save(transaction);
    }

    private Transaction transferTransaction(Transaction transaction) {
        walletService.subtractTotalBalance(transaction.getSenderUserId(), transaction.getAmount());
        walletService.addTotalBalance(transaction.getReceiverUserId(), transaction.getAmount());
        return transactionRepository.save(transaction);
    }

    private Transaction transferHeldTransaction(Transaction transaction) {
        walletService.subtractTotalHeldBalance(transaction.getSenderUserId(), transaction.getAmount());
        walletService.addTotalBalance(transaction.getReceiverUserId(), transaction.getAmount());
        return transactionRepository.save(transaction);
    }

    private Transaction holdTransaction(Transaction transaction) {
        walletService.subtractTotalBalance(transaction.getReceiverUserId(), transaction.getAmount());
        walletService.addTotalHeldBalance(transaction.getReceiverUserId(), transaction.getAmount());
        transaction.setSenderUserId(null);
        return transactionRepository.save(transaction);
    }

    private Transaction unholdTransaction(Transaction transaction) {
        walletService.subtractTotalHeldBalance(transaction.getReceiverUserId(), transaction.getAmount());
        walletService.addTotalBalance(transaction.getReceiverUserId(), transaction.getAmount());
        return transactionRepository.save(transaction);
    }





}