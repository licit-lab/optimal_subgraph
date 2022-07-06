pourc <- read.table("Documents/eclipse-workspace/SubGraphs/pourc.txt")
pourc <- pourc$V1
hist(pourc, breaks= 12, main = 'Percentage of remaining open stops', xlab = "Percentage", ylab = "# of occurences", xlim = c(0,80), ylim = c(0,25))
summary(pourc)
mean(pourc)
sd(pourc)
length(pourc)