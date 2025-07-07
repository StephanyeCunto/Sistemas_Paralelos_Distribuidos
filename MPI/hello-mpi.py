
from mpi4py import MPI

comm = MPI.COMM_WORLD

rank = comm.Get_rank()

tamanho = comm.Get_size()

print("Sou o processo inicial", rank, " o grupo tem tamanho ", tamanho)
'''
if rank == 0:
    print("Sou o processo inicial", rank)
else:
    print("Demais processos ", rank)
'''
