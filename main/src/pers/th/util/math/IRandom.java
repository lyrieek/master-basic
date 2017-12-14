package pers.th.util.math;

import java.util.concurrent.atomic.AtomicLong;

/**
 * random ·ÂÕÕÀà
 * @author ÌìºÆ
 *
 */
public class IRandom {

	private static final long multiplier = 0x5DEECE66DL;
	private static final long addend = 0xBL;
	private static final long mask = (1L << 48) - 1;

	private AtomicLong seed;
	private int bits;

	public IRandom(int seed) {
		this.seed = new AtomicLong((seed ^ multiplier) & mask);
	}

	public IRandom() {
		AtomicLong seedUniquifier = new AtomicLong(8682522807148012L);
		long next = 0;
		for (;;) {
			long current = seedUniquifier.get();
			next = current * 181783497276652981L;
			if (seedUniquifier.compareAndSet(current, next)){
				this.seed = new AtomicLong(next ^ System.nanoTime());
				return;
			}
		}
	}

	private int next() {
		long oldseed, nextseed;
		AtomicLong seed = this.seed;
		do {
			oldseed = seed.get();
			nextseed = (oldseed * multiplier + addend) & mask;
		} while (!seed.compareAndSet(oldseed, nextseed));
		return (int) (nextseed >>> (48 - bits));
	}

	public int nextInt(int n) {
		this.bits = 31;
		if ((n & -n) == n)
			return (int) ((n * (long) next()) >> bits);
		int bits, val;
		do {
			bits = next();
			val = bits % n;
		} while (bits - val + (n - 1) < 0);
		return val;
	}
	
	

}
