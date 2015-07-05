HBase Nodo singolo
-----------------

Immagine di partenza java:8-jdk


Preconfigurazione
-----
### a. Aprire VirtualBox

### b. Selezionare il container di docker o boot2docker

### c. Aprire Cartelle condivise 

### d. Aggiungere una nuova cartella chiamata "condivisa" con accesso completo e confermare

Salvare il percorso specificato poiché servirà dopo

### e. Aprire Rete e selezionare Inoltro delle porte

### f. Aggiugere un'eccezione UDP e una TCP per la porta 8080 ed IP 127.0.0.1 e confermare


Steps 
-----
### 1. Avviare e connettere docker, docker-machine o boot2docker

Es. con boot2docker scrivere nel terminale Git Bash
```bash
boot2docker start
boot2docker ssh
```


### 2. Clonare la repository ed accedervi

```bash

git clone https://github.com/GruppoPBDMNG-3/url.id.git
cd url.id/hbase

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

avvia l'immagine in modalità demone con porte 8080, 2181, 60010, 60000, 60020 e 60030 aperte e cartella condivisa
```bash

docker run -v /DISCO/PATH:/condivisa:/root/condivisa -d -p 8080:8080 -p 2181:2181 -p 60010:60010 -p 60000:60000 -p 60020:60020 -p 60030:60030 --name hbase -h hbase hbase:single

```
con PATH si intende il percorso della cartella condvisa specificato durante la fase di preconfigurazione, escluso il disco in cui si trova (esso va specificato al posto della parola DISCO) esempio: /c/Users/condivisa

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

