package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public final class DefaultTransactionIdGenerator implements	TransactionIdGenerator {
	private final AtomicInteger counter = new AtomicInteger(0);

	@Override
	public String generateTxnId() {
		StringBuilder txnId = new StringBuilder(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

		int tick = Math.abs(counter.getAndIncrement() % 100);

		if (tick < 10) {
			txnId.append("0"); 
		}

		return txnId.append(tick).toString();
	}
}
