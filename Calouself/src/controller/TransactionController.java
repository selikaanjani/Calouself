package controller;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import model.Item;
import model.Transaction;

public class TransactionController {
	private Transaction trModel = new Transaction();
	private WishlistController wc = new WishlistController();

	public String generateTransactionID() {
		return trModel.generateTransactionID();
	}

	public String createTransaction(String transactionID, String userID, String itemID) {
		return trModel.createTransaction(transactionID, userID, itemID);
	}

	public ObservableList<Transaction> viewHistory(String userID) {
		return trModel.viewHistory(userID);
	}

}
