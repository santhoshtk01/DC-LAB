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

n = 1000000  # Total intervals

if rank == 0:
    chunk = n // size
    for i in range(1, size):
        start = i * chunk
        end = n if i == size - 1 else (i+1) * chunk
        comm.send((start, end), dest=i)
    
    # Master computes its chunk
    pi = compute_pi(0, chunk, n)
    
    # Receive results from workers
    for i in range(1, size):
        partial = comm.recv(source=i)
        pi += partial
    
    print(f"[P2P] Approximated Pi: {pi}")

else:
    start, end = comm.recv(source=0)
    local_pi = compute_pi(start, end, n)
    comm.send(local_pi, dest=0)
