# cloudnativeworkshop-demojava
Demo Java for Cloud Native Workshop (inner loop and outer loop)

## How to configure the project for the inner loop with Draft
0) Requirements:
    - `choco install draft -y`
    - `choco install kubernetes-helm -y`
    - AKS cluser created on Azure 
    - ACR registry created on Azure
    - MySQL created on Azure (IaaS or PaaS, as you prefer, we recommend to use PaaS version)
    - `az login`
    - `az aks get-credentials -n <nomecluster> -g <nomerg>`
    - `helm init` configures tiller on the AKS cluster on Azure
    - `draft init` configures Draft locally
    
**ATTENTION**: In the **javapp** folder you can find the Java application, the Dockerfile and the *chart* folder with the helm charts already created. So the steps 1-2 are already done and you can procees with the step 3.

1) From the command prompt run `draft create -p java` 
2) The application uses a MySQL database so you have to modify a little bit the auto-generated charts to add the connection string
  a) Insert at the end of the file *charts/JavaApplication/templates/deployment.yaml*
    ```
          env:
              - name: host
                value: {{ .Values.env.host }}
              - name: database
                value: {{ .Values.env.database }}
              - name: username
                value: {{ .Values.env.username }}
              - name: password
                value: {{ .Values.env.password }}
    ```
    b) Insert in *charts/JavaApplication/values.yaml* the following variables
    ```
          env:
            host: <hostname>
            database: <databasename>
            username: <username>
            password: <password>
    ```
3) Configure Draft to push the built image in Azure Container Registry
    - `draft config set registry <acr_name>.azurecr.io` 
    - `docker login <acr_name>.azurecr.io` 
4) `draft up` launches a pipeline with building the image from Dockerfile, pushing the image in the configured ACR and realesing the container on the AKS cluster
13) `draft connect` to create a tunnel between your development machine and the AKS cluster. For debugging the container on AKS locally, if you are using VS Code you have to:
  a) Install the [Java Extension Pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)
  b) Add a configuration to the *launch.json* file
  ```
  {
            "type": "java",
            "name": "Debug (Attach)",
            "request": "attach",
            "hostName": "localhost",
            "port": <debug-port>
  }
  ```
