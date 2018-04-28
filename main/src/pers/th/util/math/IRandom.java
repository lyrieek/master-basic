package pers.th.util.math;

import java.util.concurrent.atomic.AtomicLong;

/**
 * java.util.Random 仿照类
 * @author 天浩
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
		long next;
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
		long oldSeed, nextSeed;
		AtomicLong seed = this.seed;
		do {
			oldSeed = seed.get();
			nextSeed = (oldSeed * multiplier + addend) & mask;
		} while (!seed.compareAndSet(oldSeed, nextSeed));
		return (int) (nextSeed >>> (48 - bits));
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
