import numpy as np
import random
from matplotlib import pyplot as plt


class Algoritmo_Genetico:

    #Metodo constructor de la clase Algoritmo_Genetico
    def __init__(self, poblacion, coeficienteMutacion, metodoCruza, numeroGeneraciones):
        self.poblacion = poblacion
        self.coeficienteMutacion = coeficienteMutacion
        self.metodoCruza =metodoCruza
        self.matriz_medidas_fit = np.zeros(len(self.poblacion))
        self.numeroGeneraciones = numeroGeneraciones
        self.poblacionDeHijos = poblacion

    #Metodo de decodificacion de elementos de la poblacion (numero binario)
    def decodificacion(self, elemento):
        numero = 0
        for i in range(0, len(elemento)):
            numero = numero + int(elemento[i]) * 2 **(-(i + 1))
        return numero

    #Metodo Matematica a optimizar, misma que usaremos para darle una medida fit a cada elemento de la poblacion, buscando el mayor valor
    def funcion_a_optimizar(self, x):
        return ((1 -((float(11)/float(2))* x - float(7)/float(2)) **2)* (np.cos((float(11)/float(2))*x - float(7)/float(2)) + 1)) + 2

    #Metodo para asignar la medida fit a cada elemento de la poblacion
    def asignar_medida_fit(self):
        suma = 0
        for i in range(0,len(self.poblacion)):
            valor = self.decodificacion(self.poblacion[i])
            self.matriz_medidas_fit[i] = self.funcion_a_optimizar(valor)
            suma = suma + self.matriz_medidas_fit[i]
        for i in range(0,len(self.poblacion)):
            self.matriz_medidas_fit[i] = self.matriz_medidas_fit[i] * 100 / float(suma)

    #Metodo para seleccionar padre y madre
    def bernouli_selection(self):
        padres = []
        i = 0
        while(i != 2):
            hijo = int(random.random() * len(self.poblacion))
            random_range = random.random() * 100
            if(random_range < self.matriz_medidas_fit[hijo]):
                padres.append(self.poblacion[hijo])
                i = i+1
        return padres[0], padres[1]

    #Metodos para el cruzamiento de los padres
    def cross_Over(self,padre, madre):
        puntoMedio = int(float(len(padre)) / float(2))
        adn1 = padre[:puntoMedio]
        adn2 = madre[puntoMedio:]
        return adn1+adn2

    def cross_over_probabilistico(self, padre, madre):
        num = int(random.random() * (len(padre) - 1))
        adn1 = padre[:num]
        adn2 = madre[num:]
        return adn1 + adn2

    def cross_over_volado(self, padre, madre):
        hijo = ''
        for i in range(0, len(padre)):
            numeroAletorio = random.random()
            if(numeroAletorio < 0.5):
                hijo = hijo + padre[i]
            else:
                hijo = hijo + madre[i]
        return hijo

    #Metodo para seleccionar el metodo de cruza
    def seleccionarMetodoCruza(self,padre, madre):
        metodosCruzamiento = {
            "cross_Over": self.cross_Over,
            "cross_over_probabilistico": self.cross_over_probabilistico,
            "cross_over_volado": self.cross_over_volado,
        }
        func = metodosCruzamiento.get(self.metodoCruza)
        return func(padre, madre)

    #Metodo para la mutacion
    def mutacion(self, hijo):
        hijo_mutado = ""
        for i in range(0, len(hijo)):
            numeroAleatorio = random.random()
            if(numeroAleatorio < self.coeficienteMutacion):
                hijo_mutado = hijo_mutado + random.choice('01')
            else:
                hijo_mutado = hijo_mutado + hijo[i]

        return hijo_mutado

    #Algoritmo Genetico
    def evolucionar(self):

        for a in range(0, self.numeroGeneraciones):
            self.asignar_medida_fit()
            for i in range(0,len(self.poblacion)):
                padre, madre = self.bernouli_selection()
                hijo = self.seleccionarMetodoCruza(padre, madre)
                hijo = self.mutacion(hijo)
                self.poblacionDeHijos[i] = hijo
            self.poblacion = self.poblacionDeHijos

#Algoritmo Genetico

poblacion = np.array(['100', '001', '000', '111', '010'])
AG = Algoritmo_Genetico(poblacion, 0.236, 'cross_over_probabilistico', 35)


for i in range(0,len(AG.poblacion)):
    plt.plot(AG.decodificacion(AG.poblacion[i]), AG.funcion_a_optimizar(AG.decodificacion(AG.poblacion[i])), color = 'red', marker = '*', markersize=12)

AG.evolucionar()

#Para mostrar la data de la poblacion detalladamente

for i in range(0, len(AG.poblacion)):
    print "Elemento: " + str(AG.poblacion[i]) + " Decodificado: "+ str(AG.decodificacion(AG.poblacion[i])) + " tiene f(x) = "+ str(AG.funcion_a_optimizar(AG.decodificacion(AG.poblacion[i])))


#Para graficar la funcion

for i in range(0,len(AG.poblacion)):
    plt.plot(AG.decodificacion(AG.poblacion[i]), AG.funcion_a_optimizar(AG.decodificacion(AG.poblacion[i])), color = 'green', marker = '*', markersize=12)

imagen = np.array([0.04, 0.08, 0.12,0.16, 0.2,
                   0.24, 0.28, 0.32, 0.36, 0.4,
                   0.44, 0.48, 0.52, 0.56, 0.60,
                   0.64, 0.68, 0.72, 0.76, 0.80,
                   0.84, 0.88, 0.92, 0.96, 1.00,
                   1.04, 1.08, 1.12, 1.16, 1.20])

plt.plot(imagen, AG.funcion_a_optimizar(imagen), 'b--')

plt.show()
