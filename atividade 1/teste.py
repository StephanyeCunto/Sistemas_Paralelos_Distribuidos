import subprocess

args=["./dobro"]

p=subprocess.Popen(args, stdin=subprocess.PIPE,stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)

saida = p.communicate("8")

print("Saida do processo - ")
print("Saida padrão - "+saida[0])
print("Saida de erro - "+saida[1])
print("Programa Python executado")
 