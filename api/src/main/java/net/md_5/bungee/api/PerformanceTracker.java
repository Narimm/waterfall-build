package net.md_5.bungee.api;

public interface PerformanceTracker {
	public boolean isEnabled();
	public void setEnabled(boolean enabled);

	public PerformanceStatistics getStatistics();
	public PerformanceStatistics getAndResetStatistics();
	public void resetStatistics();
}
