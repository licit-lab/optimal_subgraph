before <- scan("Documents/eclipse-workspace/SubGraphs/trajBefore.txt")

after <- scan("Documents/eclipse-workspace/SubGraphs/trajAfter.txt")

diff <- after-before;

diffNonZero <- diff[diff != 0];

hist(diffNonZero, breaks = 50, xlim = c(-400/60, 800/60), xlab = "Difference between total travel time before and after exclusion of bus stops (in min)", main = "Histogram of the delay induced by Vsol")