from mpi4py import MPI
import numpy as np

comm = MPI.COMM_WORLD
rank = comm.Get_rank()
size = comm.Get_size()

m, n, p = 4, 4, 4  # A is m x n, B is n x p

if rank == 0:
    A = np.random.rand(m, n)
    B = np.random.rand(n, p)
    
    rows_per_proc = m // size
    for i in range(1, size):
        start = i * rows_per_proc
        end = m if i == size - 1 else (i+1) * rows_per_proc
        comm.send(A[start:end], dest=i)
        comm.send(B, dest=i)
    
    local_C = np.dot(A[0:rows_per_proc], B)
    C = [local_C]
    
    for i in range(1, size):
        recv_C = comm.recv(source=i)
        C.append(recv_C)
    
    C = np.vstack(C)
    print("[P2P] Result Matrix C:\n", C)

else:
    A_chunk = comm.recv(source=0)
    B = comm.recv(source=0)
    C_chunk = np.dot(A_chunk, B)
    comm.send(C_chunk, dest=0)
