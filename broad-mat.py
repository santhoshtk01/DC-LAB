from mpi4py import MPI
import numpy as np

comm = MPI.COMM_WORLD
rank = comm.Get_rank()
size = comm.Get_size()

m, n, p = 4, 4, 4  # A is m x n, B is n x p
rows_per_proc = m // size

if rank == 0:
    A = np.random.rand(m, n)
    B = np.random.rand(n, p)
else:
    A = None
    B = np.empty((n, p), dtype='d')

# Scatter rows of A
local_A = np.empty((rows_per_proc, n), dtype='d')
comm.Scatter(A, local_A, root=0)

# Broadcast B to all
comm.Bcast(B, root=0)

# Each process computes its part of C
local_C = np.dot(local_A, B)

# Gather results
C = None
if rank == 0:
    C = np.empty((m, p), dtype='d')
comm.Gather(local_C, C, root=0)

if rank == 0:
    print("[Bcast/Scatter/Gather] Result Matrix C:\n", C)
