HBase Nodo singolo
-----------------

Immagine di partenza java:8-jdk

Steps 
-----
### 0. Avviare e connettere docker, docker-machime o boot2docker

Es. con boot2docker scrivere nel terminale Git Bash
```bash
boot2docker start
boot2docker ssh
```

### 1. Creare una cartella ed accedervi

Creare una cartella in cui scaricare la repository

```bash

mkdir laboratorio
cd laboratorio

```

### 2. Clonare la repository ed accedervi

```bash

git clone https://github.com/fabiodepasquale/hbase.git
cd hbase

```


### 3. Montare l'immagine

Fare il build dell'immagine

```bash
 docker build --tag=hbase:single ./
```

Su boot2docker usare questo
```bash
 sudo docker build --tag=hbase:single ./
```


### 4. Avviare l'immagine

avvia l'immagine in modalità demone con porte 2181, 60010, 60000, 60020 e 60030 aperte
```bash

docker run -d -p 2181:2181 -p 60010:60010 -p 60000:60000 -p 60020:60020 -p 60030:60030 --name hbase -h hbase hbase:single

```

### Nota

1.	Con boot2docker o docker-machine aggiungere il comando

```bash

sudo nano private/etc/hosts

and add <ip docker machine> hbase-single
```

### 5. Connessione all'immagine 

```bash

docker exec -it hbase bash

```

### 6. Avviare la shell

```bash

hbase shell

```

