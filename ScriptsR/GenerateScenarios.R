x0 <- scan("/Users/matthieu/Documents/eclipse-workspace/SubGraphs/hist_Huf_0.0.txt")
x1 <- scan("/Users/matthieu/Documents/eclipse-workspace/SubGraphs/hist_Huf_0.1.txt")
x2 <- scan("/Users/matthieu/Documents/eclipse-workspace/SubGraphs/hist_Huf_0.2.txt")
x3 <- scan("/Users/matthieu/Documents/eclipse-workspace/SubGraphs/hist_Huf_0.3.txt")
x4 <- scan("/Users/matthieu/Documents/eclipse-workspace/SubGraphs/hist_Huf_0.4.txt")
x5 <- scan("/Users/matthieu/Documents/eclipse-workspace/SubGraphs/hist_Huf_0.5.txt")
x6 <- scan("/Users/matthieu/Documents/eclipse-workspace/SubGraphs/hist_Huf_0.6.txt")
x7 <- scan("/Users/matthieu/Documents/eclipse-workspace/SubGraphs/hist_Huf_0.7.txt")
x8 <- scan("/Users/matthieu/Documents/eclipse-workspace/SubGraphs/hist_Huf_0.8.txt")
x9 <- scan("/Users/matthieu/Documents/eclipse-workspace/SubGraphs/hist_Huf_0.9.txt")
x10 <- scan("/Users/matthieu/Documents/eclipse-workspace/SubGraphs/hist_Huf_1.txt")

xok = scan("/Users/matthieu/Documents/eclipse-workspace/SubGraphs/hist_Huf_0.55.txt")

x0 <- x0/60
x1 <- x1/60
x2 <- x2/60
x3 <- x3/60
x4 <- x4/60
x5 <- x5/60
x6 <- x6/60
x7 <- x7/60
x8 <- x8/60
x9 <- x9/60
x10 <- x10/60

xlimR = c(0,50)
ylimR = c(0,400)
hist(x10, breaks =8, main = 'Increase of access time for violated constraints', xlab = "Delay (min)", ylab = "# of occurences", xlim = xlimR, ylim = ylimR, cex.main=3, cex.lab=1.5, cex.sub=1.5, font.main=2, font.lab=2, font.sub=2)


