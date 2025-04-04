from mpi4py import MPI

def compute_pi(start, end, n):
    h = 1.0 / n
    local_sum = 0.0
    for i in range(start, end):
        x = h * (i + 0.5)
        local_sum += 4.0 / (1.0 + x*x)
    return local_sum * h

comm = MPI.COMM_WORLD
rank = comm.Get_rank()
size = comm.Get_size()

n = 1000000
n = comm.bcast(n, root=0)

chunk = n // size
start = rank * chunk
end = n if rank == size - 1 else (rank + 1) * chunk

local_pi = compute_pi(start, end, n)

pi = comm.reduce(local_pi, op=MPI.SUM, root=0)

if rank == 0:
    print(f"[Broadcast/Reduce] Approximated Pi: {pi}")
