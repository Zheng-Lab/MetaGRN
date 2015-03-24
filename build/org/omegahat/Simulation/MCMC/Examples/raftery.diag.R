"raftery.diag" <-
  function (data, q = 0.025, r = 0.0125, s = 0.95, converge.eps = 0.001) 
{
  if (is.mcmc.list(data))
    {
      retval _ (lapply(data, raftery.diag, q, r, s, converge.eps))
      omax _ retval[[1]]
      omax$resmatrix _ omax$resmatrix + NA;  # to make sure that nothing carries over by accident
      for(i in 1:dim(omax$resmatrix)[1] )
        omax$resmatrix[i,] _ apply(sapply( retval, function(x) x$"resmatrix"[i,]),1,max)
      retval$"Overall Maximum" _ omax

      omedian _ retval[[1]]
      omedian$resmatrix _ omedian$resmatrix + NA;  # to make sure that nothing carries over by accident
      for(i in 1:dim(omedian$resmatrix)[1] )
        omedian$resmatrix[i,] _ apply(sapply( retval, function(x) x$"resmatrix"[i,]),1,median)
      retval$"Overall Median" _ omedian

      omin _ retval[[1]]
      omin$resmatrix _ omin$resmatrix + NA;  # to make sure that nothing carries over by accident
      for(i in 1:dim(omedian$resmatrix)[1] )
        omin$resmatrix[i,] _ apply(sapply( retval, function(x) x$"resmatrix"[i,]),1,min)
      retval$"Overall Min" _ omin

      return(retval)
    }
      
  data <- as.mcmc(data)
  resmatrix <- matrix(nrow = nvar(data), ncol = 4, dimnames = list(varnames(data, 
                                                     allow.null = TRUE), c("M", "N", "Nmin", "I")))
  phi <- qnorm(0.5 * (1 + s))
  nmin <- as.integer(ceiling((q * (1 - q) * phi^2)/r^2))
  if (nmin > niter(data)) 
    resmatrix <- c("Error", nmin)
  else for (i in 1:nvar(data)) {
    if (is.matrix(data)) {
      quant <- quantile(data[, i, drop = TRUE], probs = q)
      dichot <- mcmc(data[, i, drop = TRUE] <= quant, start = start(data), 
                     end = end(data), thin = thin(data))
    }
    else {
      quant <- quantile(data, probs = q)
      dichot <- mcmc(data <= quant, start = start(data), 
                     end = end(data), thin = thin(data))
    }
    kthin <- 0
    bic <- 1
    while (bic >= 0) {
      kthin <- kthin + thin(data)
      testres <- as.vector(window.mcmc(dichot, thin = kthin))
      newdim <- length(testres)
      testtran <- table(testres[1:(newdim - 2)], testres[2:(newdim - 
                                                            1)], testres[3:newdim])
      testtran <- array(as.double(testtran), dim = dim(testtran))
      g2 <- 0
      for (i1 in 1:2) {
        for (i2 in 1:2) {
          for (i3 in 1:2) {
            if (testtran[i1, i2, i3] != 0) {
              fitted <- (sum(testtran[i1, i2, 1:2]) * 
                         sum(testtran[1:2, i2, i3]))/(sum(testtran[1:2, 
                                                                   i2, 1:2]))
              g2 <- g2 + testtran[i1, i2, i3] * log(testtran[i1, 
                                                             i2, i3]/fitted) * 2
            }
          }
        }
      }
      bic <- g2 - log(newdim - 2) * 2
    }
    finaltran <- table(testres[1:(newdim - 1)], testres[2:newdim])
    alpha <- finaltran[1, 2]/(finaltran[1, 1] + finaltran[1, 
                                                          2])
    beta <- finaltran[2, 1]/(finaltran[2, 1] + finaltran[2, 
                                                         2])
    tempburn <- log((converge.eps * (alpha + beta))/max(alpha, 
                                                        beta))/(log(abs(1 - alpha - beta)))
    nburn <- as.integer(ceiling(tempburn) * kthin)
    tempprec <- ((2 - alpha - beta) * alpha * beta * phi^2)/(((alpha + 
                                                               beta)^3) * r^2)
    nkeep <- as.integer(ceiling(tempprec) * kthin)
    iratio <- (nburn + nkeep)/nmin
    resmatrix[i, 1] <- nburn
    resmatrix[i, 2] <- nkeep + nburn
    resmatrix[i, 3] <- nmin
    resmatrix[i, 4] <- signif(iratio, digits = 3)
  }
  y <- list(params = c(r = r, s = s, q = q), resmatrix = resmatrix)
  class(y) <- "raftery.diag"
  return(y)
}
