package uk.dansiviter.scd;

public record Pair<F, S> (F first, S second) {
	public static <F, S> Pair<F, S> pair(F first, S second) {
		return new Pair<>(first, second);
	}
}
