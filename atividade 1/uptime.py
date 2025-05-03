import subprocess

args=["uptime"]

p=subprocess.Popen(args, stdin=subprocess.PIPE,stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)

saida = p.communicate()

print("Saida do processo - "+saida[0])

loads = saida[0].split("load averages:")[-1]
load_values = loads.strip().split(" ")

if "load averages" in saida[0]:
    for i in range(3):
        load_values[i] = float(load_values[i].replace(",", "."))

        if(load_values[i]>1):
            print("Carga alta: "+str(load_values[i]))
        else:
            print("Carga baixa: "+str(load_values[i]))