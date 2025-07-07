import numpy

TAMANHODOVETOR = 10000
vetor = numpy.arange(TAMANHODOVETOR)

def soma(inicio, fim):
	s = numpy.zeros(1)
	for x in range(inicio, fim, 1):
		s = s + vetor[x]
	return s

total = numpy.zeros(1)
total = soma(0, TAMANHODOVETOR)
print( "A soma do vetor eh: ", total)
