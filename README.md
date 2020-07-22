# MiMedicacion
Aplicacion con usuarios, que sirve para que el usuario guarde su medicación y la aplicación le recuerde cuando tomarla

### BD: MySQL

### Back-end: API Rest con Java web application y Maven
<ul>
  <li>jersey-server 2.31</li>
  <li>jersey-containers-servlet-core 2.31</li>
  <li>jaxrs-ri 2.31</li>
  <li>jersey-media-multipart 2.31</li>
  <li>mysql-connector-java 8.0.21</li>
  <li>jjwt-api 0.11.2</li>
  <li>jjwt-impl 0.11.2</li>
  <li>jjwt-jackson 0.11.2</li>
</ul>

<p>documentación del servidor en javadocs: dentro de MiMedicacion/target/site/apidocs, https://github.com/Mbg999/MiMedicacion/tree/master/MiMedicacion/target/site/apidocs</p>
<p>documentación de las rutas de la API: https://documenter.getpostman.com/view/4552748/T1DjjzaA?version=latest</p>

<p>no olvides crear el árbol de directorios <code>C:\MiMedicacionAssets\users\pictures</code> o modificar el path al que tu prefieras desde <code>MiMedicacion/src/main/java/com/miguel/mimedicacion/utils/Upload.java</code></p>
<p>Añadir la imagen <code>backend/nopicture.png</code> al directorio anterior</p>

### Front-end: Angular 10 + ngBootstrap + @mdi/font icons (material design icons) + SweetAlert2
<p><code>npm i</code> para instalar <code>node_modules</code></p>
<p>El back-end ha sido desarrollado usando el GlashFish Server de NetBeans, por lo que la URL del back-end es http://localhost:8080/MiMedicacion , puedes cambiarla en el archivo environment de Angular en caso de que tu URL sea distinta</p>

#### Video de presentación
<a href="https://youtube.com/watch?v=WF1quJBogGU" target="_blank">
  <img src="https://img.youtube.com/vi/WF1quJBogGU/0.jpg" title="video de Mi Medicación" alt="video de Mi Medicación">
</a>

#### Algunas imágenes

<div>
  <img src="https://i.imgur.com/o5ISb7O.png" alt="registro" title="registro">
  <img src="https://i.imgur.com/7vNNE8E.png" alt="inicio de sesion" title="inicio de sesion">
</div>
<div>
  <img src="https://i.imgur.com/SPKfCr3.png" alt="Actualizar usuario" title="Actualizar usuario">
  <img src="https://i.imgur.com/E6AmG5g.png" alt="home con notificacion" title="home con notificacion">
</div>
<div>
  <img src="https://i.imgur.com/JmhB6ID.png" alt="Registrar medicacion" title="Registrar medicacion">
  <img src="https://i.imgur.com/DlyE25a.png" alt="responsive" title="responsive">
</div>
