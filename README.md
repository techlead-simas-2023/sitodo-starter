# Sitodo Starter Code

Contoh proyek aplikasi todo list sederhana untuk mendemonstrasikan _testing_ dari tingkat _unit_ hingga _functional_.
Proyek ini juga mengilustrasikan standar pengembangan berkualitas tinggi dengan menerapkan CI/CD dan penjaminan mutu kualitas kode.

> TODO: Silakan tambahkan _status badges_ SonarCloud proyek Sitodo anda di sini.

## Persiapan Hands-On

Persiapkan _tools_ berikut di komputer anda:

- [Git](https://git-scm.com/)
- [Java JDK 17](https://adoptium.net/temurin/releases/?version=17)
- [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download/)
- _Web browser_ [Mozilla Firefox](https://www.mozilla.org/en-US/firefox/new/)
  dan [Google Chrome](https://www.google.com/chrome/index.html).
- (Opsional) [Apache Maven](https://maven.apache.org/download.cgi)
- (Opsional) [Docker dan Docker Compose](https://docs.docker.com/desktop/) apabila ingin menjalankan contoh proyek di dalam _container_.

Pastikan anda dapat memanggil program-program berikut dari dalam _shell_ favorit anda:

```shell
git --version
java --version
javac --version
```

Hasil pemanggilan program-program di atas seharusnya akan mencetak versi program yang terpasang di komputer anda.

Selain itu, pastikan anda telah memiliki akun di [GitHub](https://github.com), [SonarCloud](https://sonarcloud.io),
dan [Heroku](https://heroku.com). Pastikan anda bisa berhasil login ke masing-masing sistem.
Jika sudah berhasil login ke GitHub dan Heroku,
harap kumpulkan nama _username_ GitHub dan alamat email yang dipakai untuk pendaftaran akun Heroku ke slot pengumpulan terkait di LMS.

Mari mulai dengan menyalin kode contoh proyek yang akan dibahas di sesi hands-on hari ini,
yaitu proyek [Sitodo](https://github.com/techlead-simas-2023/sitodo-starter).
Buka laman proyek Sitodo kemudian klik tombol "Use this template" untuk membuat salinan proyek tersebut ke akun GitHub anda.

Apabila sudah membuat salinan proyek Sitodo ke akun pribadi GitHub anda,
buka laman proyek tersebut di GitHub dan salin repositori kode proyeknya ke suatu lokasi di sistem berkas komputer anda menggunakan Git:

```shell
# Contoh perintah Git untuk membuat salinan repositori ke dalam sebuah folder
# baru bernama `sitodo-techlead-simas` di dalam direktori home:
git clone git@github.com:[ akun GitHub anda ]/sitodo-techlead-simas.git ~/sitodo-techlead-simas
```

Jika anda lebih nyaman menggunakan IDE seperti IntelliJ IDEA,
anda juga dapat menyalin repositori kode proyek melalui tombol "Get from VCS" seperti yang digambarkan pada _screenshot_ berikut:

![Tampilan tombol "Get from VCS"](https://pmpl.cs.ui.ac.id/workshops/images/day_1_sqa_-_get_from_vcs.png)

Selanjutnya, buka kode proyek menggunakan IntelliJ IDEA.
Kode proyek yang akan dibahas di hari ini adalah aplikasi "Sitodo",
yaitu aplikasi todo list sederhana yang dibangun menggunakan _framework_ Spring Boot
dan digunakan sebagai _running example_ di dalam mata kuliah [Penjaminan Mutu Perangkat Lunak di Fasilkom UI](https://pmpl.cs.ui.ac.id).

Panduan untuk membuat _build_ serta menjalankan aplikasi dapat dibaca secara mandiri di dokumentasi proyek ([`README.md`](./README.md).
Namun untuk keperluan pelatihan hari ini, anda tidak perlu memasang database PostgreSQL yang dibutuhkan oleh aplikasi.
Sebagai gantinya, anda akan menggunakan database _in-memory_ bernama HSQLDB yang akan selalu di-_reset_ setiap kali aplikasi dimatikan.

Untuk membuat _build_ dan menjalankan aplikasinya secara lokal menggunakan database HSQLDB,
panggil perintah Maven untuk membuat _build_ terlebih dahulu:

```shell
./mvnw package
```

> Catatan: Jika ada masalah ketika menjalankan test secara lokal, maka perintah Maven bisa ditambahkan opsi
> `-DskipTests` agar proses pembuatan berkas JAR tidak menjalankan _test suite_.
> Jika anda ingin menjalankan _test suite_ saja tanpa membuat JAR, anda juga dapat menggunakan perintah Maven berikut:
> `./mvnw test`

Kemudian jalankan berkas JAR aplikasinya:

```shell
java -jar ./target/sitodo-0.2.5-SNAPSHOT.jar
```

Aplikasi akan jalan dan dapat diakses melalui alamat [`http://127.0.0.1:8080`](http://127.0.0.1:8080).
Apabila sudah ada aplikasi lain yang jalan di alamat yang sama (misal: bentrok nomor _port_),
tambahkan parameter `-D"server.port=<nomor port lain>` ketika memanggil perintah Maven.

Selanjutnya, coba menjalankan fitur utama aplikasi, yaitu membuat todo list.
Tambahkan beberapa _item_ baru ke dalam todo list.
Kemudian perhatikan kondisi-kondisi pada aplikasi, seperti:

- Alamat (URL) yang tercantum di _address bar_ pada _web browser_ yang anda gunakan.
- Pesan yang muncul setelah anda mengubah status penyelesaian _item_ di dalam todo list.
- URL aplikasi ketika anda melakukan _refresh_ atau mengunjungi kembali aplikasi di alamat [`http://127.0.0.1:8080`](http://127.0.0.1:8080).

## Test Automation

Langkah-langkah percobaan yang anda lakukan sebelumnya mungkin berbeda dengan apa yang dilakukan oleh kolega anda.
Mungkin anda membuat _item_ baru dengan mengetikkan _item_ tersebut kemudian anda menekan tombol "Enter" di keyboard.
Sedangkan kolega anda tidak menekan tombol "Enter" ketika membuat _item_ baru, melainkan menekan tombol "Enter" yang ada di halaman aplikasi.
Mungkin skenario di atas terdengar sepele, namun menggambarkan adanya potensi proses uji coba dilakukan secara tidak konsisten jika dilakukan oleh manusia.

Langkah-langkah yang cenderung repetitif dapat diotomasi dan dijalankan oleh bantuan program test.
Program tidak akan "lelah" ketika harus menjalankan instruksi yang sama berulang kali.
Bayangkan fitur membuat todo list baru tersebut diuji coba secara otomatis setiap kali ada perubahan baru pada kode proyek.
Tim pengembang dapat lebih fokus untuk menyelesaikan fitur-fitur yang dibutuhkan
dan menyiapkan prosedur uji coba yang dibutuhkan untuk dijalankan secara otomatis.

Saat ini kode proyek Sitodo telah memiliki kumpulan _test suite_,
yaitu kumpulan _test case_ yang dapat dijalankan sebagai program test oleh _test runner_ terhadap subjek uji coba.
Subjek uji coba berupa _software_/sistem secara utuh (seringkali disebut sebagai _System/Software Under Test_ atau SUT).

## Struktur Test Case

Sebuah _test case_ yang diimplementasikan sebagai program test biasanya akan memiliki struktur yang terdiri dari empat bagian prosedur,
yaitu:

1. Setup - menyiapkan _testing environment_ dan SUT ke kondisi siap diuji coba, termasuk menyiapkan nilai masukan _test case_
2. Exercise - menjalankan skenario uji coba pada SUT
3. Verify - membuktikan hasil skenario uji coba pada SUT dengan hasil yang diharapkan
4. Teardown - mengembalikan kondisi _testing environment_ dan SUT ke kondisi awal sebelum uji coba

Mari coba identifikasi keempat bagian tersebut pada dua contoh _test case_.
Pertama, lihat _test case_ berikut yang membuktikan kebenaran _method_ `equals` pada _class_ `TodoItem`:

```java
@Test
void testEquals() {
    // Setup
    TodoItem first = new TodoItem("Buy milk");
    TodoItem second = new TodoItem("Cut grass");

    // Exercise (implisit) & Verify
    assertNotEquals(first, second);

    // Tidak ada Teardown secara eksplisit
}
```

_Setup_ mengandung instruksi untuk menyiapkan SUT, yaitu membuat dua buah objek `TodoItem` yang berperan sebagai subjek yang akan diujicobakan.
Kemudian _Exercise_ dilakukan secara implisit ketika _Verify_ dilakukan pada contoh di atas,
yaitu pemanggilan `assertNotEquals` akan memanggil implementasi `equals` milik masing-masing SUT
dan membandingkan hasil kembaliannya.
Pada contoh di atas, tidak ada prosedur _Teardown_ secara eksplisit.
Namun, anda bisa menganggap proses _garbage collection_ yang dilakukan _runtime_ Java (JVM) di akhir eksekusi test sebagai prosedur _Teardown_.

Mari lihat contoh _test case_ lain yang lebih kompleks, yaitu _test case_ untuk _class_ `MainController`:

```java
@WebMvcTest(MainController.class)   // <-- Setup
class MainControllerTest {

    @Autowired  // <-- Setup
    private MockMvc mockMvc;

    @Test
    @DisplayName("HTTP GET '/' redirects to '/list")
    void showMainPage_resolvesToIndex() throws Exception {
        mockMvc.perform(get("/"))   // <-- Exercise
               .andExpectAll(   // <-- Verify
                   status().is3xxRedirection(),
                   redirectedUrl("/list")
               );
        // Tidak ada Teardown secara eksplisit
    }
}
```

Prosedur _Setup_ pada _test case_ di atas melakukan:

1. `@WebMvcTest` menyiapkan _environment_ minimalis berupa server untuk menjalankan SUT (yaitu: objek `MainController`)
   beserta _dependency_ yang dibutuhkan oleh SUT.
2. `@Autowired` menyiapkan objek _mock_ bertipe `MockMvc` sebagai _client_ untuk menyimulasikan pertukaran pesan HTTP Request & Response terhadap SUT.

Sedangkan prosedur _Exercise_ cukup jelas, yaitu menggunakan `mockMvc` untuk mengirimkan _request_ HTTP GET ke URL `/`.
_Request_ HTTP GET tersebut akan diterima oleh SUT, yaitu objek `MainController`.
Kemudian prosedur _Verify_ mengandung beberapa kondisi akhir yang akan dibuktikan dengan menginspeksi HTTP Response yang dikembalikan oleh SUT.

Setelah mengetahui struktur _test case_ secara umum,
mari membahas TDD secara garis besar dengan melihat contoh beberapa _test_,
yaitu [_unit test_](#unit-test) dan [_functional test_](#functional-test).

## Unit Test

Mari coba lihat contoh _test suite_ yang termasuk dalam golongan _unit test_.
_Unit_ dalam konteks ini mengacu pada komponen terkecil pada _software_.
Sebagai contoh, fungsi dan metode (_method_) dapat diklasifikasikan sebagai _unit_.

Jalankan perintah Maven berikut di _shell_ untuk menjalankan _test suite_ bertipe `unit`:

```shell
./mvnw test -D"groups=unit"
```

Maven akan menjalankan _test suite_ yang berisi kumpulan _test case_ dari grup `@Tag("unit")` di kode test.
Hasil eksekusi setiap _test case_ kemudian dilaporkan ke _standard output_
dan berkas-berkas laporan di folder `target/surefire-reports`.

## Functional Test

Sekarang coba jalankan _test suite_ untuk menguji fungsionalitas pada SUT,
atau seringkali dikenal sebagai _functional test_.
Pengujian dilakukan terhadap SUT yang sudah di-_build_ dan berjalan di sebuah _environment_.

Jalankan perintah Maven berikut di _shell_:

```shell
./mvnw test -D"groups=func"
```

Serupa dengan contoh eksekusi sebelumnya, Maven akan menjalankan _test suite_ yang berisi kumpulan _test case_ dari grup `@Tag("func")` di kode test.
_Test suite_ jenis ini disebut sebagai _functional test_, dimana _test case_ akan menggunakan _web browser_ untuk menjalankan aksi-aksi pengguna terhadap SUT.
Pada contoh aplikasi Sitodo, aksi-aksi pengguna dijalankan pada _web browser_ secara otomatis dengan bantuan _library_ [Selenium](https://www.selenium.dev/).
Oleh karena itu, anda akan melihat _web browser_ anda bergerak secara otomatis ketika _functional test_ berjalan.

Jika anda ingin menjalankan seluruh _test suite_, maka perintah Maven yang dapat anda panggil adalah sebagai berikut:

```shell
./mvnw test
```

_Unit test_ akan berjalan sangat cepat dimana durasi tiap eksekusi _test case_ berada dalam rentang kurang dari 1 detik per _test case_.
Sedangkan _functional test_ akan memakan waktu lebih lama karena ada _overhead_ untuk menyiapkan dan menyimulasikan aksi pengguna di _web browser_.

## Laporan Hasil Test

_Test suite_ pada proyek Sitodo dibuat dengan bantuan _test framework_ JUnit 5.
Sebagai _test framework_, JUnit 5 memberikan kerangka kepada developer agar dapat membuat _test suite_ sesuai dengan konvensi JUnit 5.
Selain itu, JUnit 5 juga menyediakan _test runner_ untuk menjalankan _test suite_
serta dapat melakukan _test reporting_ untuk mencatat hasil eksekusi _test suite_.

Laporan hasil test dapat dilihat di folder `target/surefire-reports`.
Anda dapat temukan berkas-berkas laporan dalam format teks polos (`.txt`) dan XML.
Berkas laporan teks polos hanya menyebutkan berapa banyak _test case_ yang berhasil dan gagal pada sebuah _test suite_.
Sedangkan berkas laporan XML mengandung informasi yang jauh lebih rinci,
seperti informasi _environment_ yang menjalankan _test case_
hingga cuplikan log _standard output_ ketika menjalankan _test case_.

Berkas-berkas laporan tersebut dapat dikumpulkan dan diberikan ke _tools_ lain.
Sebagai contoh, versi _upstream_ (asli) proyek ini memiliki alur CI/CD yang melaporkan hasil _test_ serta analisis kode ke _tools_ bernama [SonarCloud](https://sonarcloud.io).
Hasil analisis SonarCloud versi _upstream_ dapat dilihat melalui laman SonarCloud yang dapat diakses dari kedua _badge_ berikut:

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=addianto_sitodo&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=addianto_sitodo)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=addianto_sitodo&metric=coverage)](https://sonarcloud.io/summary/new_code?id=addianto_sitodo)

Selain itu, laporan-laporan hasil _test_ juga dipublikasikan sebagai _static site_ pada GitHub Pages.
Contoh hasil _static site_ versi asli dapat dilihat di tautan berikut: [Sitodo Test Reports](https://addianto.github.io/sitodo/)

## Konfigurasi CI/CD

Untuk keperluan pelatihan hari ini, konfigurasi CI/CD sudah diberikan dan dapat dilihat di dalam folder `.github/workflows`.
CI/CD telah diatur agar melakukan aksi-aksi berikut:

1. Otomasi _build_ dan _test_ pada proyek Sitodo dimana seluruh _test suite_ akan dijalankan oleh GitHub Actions (_platform_ CI/CD bawaan GitHub)
   pada _branch_ yang menjadi bagian Pull Request ke _branch_ utama (`main`).
2. Agregasi laporan-laporan _test_ dan analisis kualitas kode ke SonarCloud.
4. Publikasi laporan-laporan _test_ ke GitHub Pages.

Di luar konfigurasi CI/CD di GitHub, proyek ini juga dapat di-_deploy_ secara otomatis ke layanan PaaS bernama [Heroku](https://heroku.com).
Konfigurasi _deployment_ diatur di Heroku sehingga Heroku akan _deploy_ versi terbaru proyek pada _branch_ utama setiap kali ada perubahan.

### Latihan: Melengkapi Konfigurasi CI/CD

Walaupun sudah ada konfigurasi CI/CD, ada beberapa aksi di dalam alur CI/CD yang masih akan gagal apabila dijalankan.
Sebagai contoh, saat ini aksi agregasi laporan test dan analisis kualitas kode ke SonarCloud pasti gagal karena membutuhkan ID dan token yang unik bagi tiap peserta pelatihan.
Aksi _deployment_ juga belum berfungsi karena anda perlu didaftarkan ke dalam tim Heroku (_Heroku Teams_) khusus untuk pelatihan ini dan mengasosiasikan salinan proyek anda agar di-_deploy_ ke tim Heroku tersebut.

1. Buat _branch_ baru bernama `ci-cd` pada repositori Git proyek Sitodo di komputer anda.
2. Buka berkas `pom.xml` di dalam proyek menggunakan _text editor_/IDE dan cari elemen (_tag_) bernama `sonar.organization` dan `sonar.projectKey`.
   Saat ini kedua elemen konfigurasi tersebut masih kosong dan perlu diisi dengan nilai yang diperoleh dari SonarCloud.
3. Buka [SonarCloud](https://sonarcloud.io) dan login menggunakan akun GitHub anda.
4. Buat proyek analisis baru di SonarCloud dengan menekan tombol "Analyze new project". Kemudian pilih "Organization" sesuai dengan nama akun GitHub anda dan pilih repositori proyek Sitodo dari daftar repositori.
5. Pilih opsi "Previous version" pada laman pengaturan "Clean as You Code".
6. Selanjutnya, SonarCloud otomatis akan menganalisis versi terkini secara otomatis.
   Untuk kebutuhan pelatihan ini, kita akan menggunakan cara manual yang dipicu dari CI/CD yang berjalan di GitHub.
7. Di laman proyek analisis, pilih menu Information kemudian catat Project Key dan Organization Key.
   Kedua buah nilai tersebut akan digunakan untuk mengisi `sonar.organization` dan `sonar.projectKey` di berkas `pom.xml`.
8. Silakan _browsing_ laman proyek analisis SonarCloud. Anda dapat melihat hasil analisis sementara SonarCloud berdasarkan versi terkini di _branch_ utama.
9. Sekarang masuk ke menu "Administration" - "Analysis Method" pada laman proyek analisis di SonarCloud.
   Matikan metode analisis otomatis, kemudian pilih metode analisis manual ("Manually").
   Kemudian pilih opsi Maven dan catat nama variabel `SONAR_TOKEN` beserta nilainya.
   Variabel tersebut akan dimasukkan sebagai "Repository Secret" di salinan proyek Sitodo di akun GitHub anda.
9. Kembali buka berkas `pom.xml`, kemudian isi elemen `sonar.organization` dan `sonar.projectKey` dengan nilai yang anda catat dari SonarCloud.
10. Simpan berkas `pom.xml` sebagai _commit_ Git baru. Kemudian _push_ _commit_ tersebut ke GitHub menggunakan perintah `git push` atau _shortcut_ yang disediakan _text editor_/IDE anda.
11. Buka laman salinan proyek Sitodo di akun GitHub anda, lalu masuk ke menu Settings - Secrets and variables dan pilih "Actions".
12. Buat sebuah "Repository Secret" baru bernama `SONAR_TOKEN` dengan nilai yang anda catat dari SonarCloud.
13. Buat sebuah Pull Request baru untuk menggabungkan (_merge_) _branch_ `ci-cd` ke _branch_ utama (`main`).
14. Buka laman Pull Request baru tersebut dan tunggu CI/CD di GitHub Actions selesai.

Setelah mengatur CI/CD di atas, maka langkah selanjutnya adalah menyiapkan _deployment_ otomatis di Heroku.

1. Login ke Heroku menggunakan akun anda dan klik _dropdown icon_ "Personal".
   Pastikan ada tim `pusilkom-training` di dalam daftar dan pilih tim tersebut.
   Jika belum ada, maka hubungi instruktur untuk dapat diundang ke dalam tim.
2. Setelah masuk ke dalam tim `pusilkom-training`, buat _app_ baru menggunakan tombol "New" - "Create new app".
3. Silakan isi nama aplikasi dengan nama aplikasi diawali prefiks nama anda, misal: `bambang-sitodo`.
   Kemudian, pastikan pemilik aplikasinya adalah tim `pusilkom-training`. Lalu saat ini acuhkan saja pilihan Region dan Pipeline.
4. Di laman berikutnya, pilih metode _deployment_ GitHub dan cari nama repositori proyek salinan Sitodo anda.
5. Pastikan _branch_ yang di-_deploy_ otomatis adalah _branch_ utama (`main`).
   Kemudian nyalakan opsi "Wait for CI to pass before deploy" untuk memastikan _deploy_ hanya terjadi jika alur CI/CD di GitHub sudah selesai.

Sampai tahap ini, anda telah mengatur konfigurasi CI/CD yang berjalan di GitHub
serta membuat _deployment_ otomatis di Heroku yang baru akan berjalan setelah CI/CD sukses.
Sekarang buka kembali Pull Request yang anda buat di GitHub,
lalu pilih untuk Merge agar konfigurasi `pom.xml` anda masuk ke _branch_ utama.
Jika tidak ada masalah, maka CI/CD akan berjalan hingga menjalankan _deploy_ otomatis ke Heroku.

### Latihan Opsional: Status Badges

Sebelumnya anda telah melihat contoh _status badges_ di dokumen ini:

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=addianto_sitodo&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=addianto_sitodo)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=addianto_sitodo&metric=coverage)](https://sonarcloud.io/summary/new_code?id=addianto_sitodo)

_Status badges_ tersebut adalah indikator visual dari SonarCloud terhadap versi _upstream_ (asli) dari proyek Sitodo.
Anda juga dapat membuat _status badges_ serupa pada salinan proyek Sitodo milik anda dengan kembali membuka laman proyek analisis proyek ini di SonarCloud, kemudian buka laman Information. Anda kemudian dapat memilih _badges_ untuk disalin ke dokumen `README.md` proyek Sitodo anda.

Silakan tambahkan _badges_ di awal dokumen ini dengan _badges_ dari proyek analisis anda dari SonarCloud.

## Siklus TDD

Proses pengembangan yang menerapkan metode TDD akan melalui tiga fase berikut secara iteratif ketika mengimplementasikan sebuah fitur,
yaitu:

1. Fase "Red"
2. Fase "Green"
3. Fase "Refactor"

Fase "Red" adalah fase awal dimana developer mengembangkan _test case_ untuk implementasi sebuah fitur terlebih dahulu.
"Red" mengacu pada status gagal/_fail_ yang dilaporkan oleh _test runner_ secara visual.
Tentu saja hasil test akan gagal karena kode implementasi masih kosong.

Setelah mengembangkan _test case_ yang dibutuhkan, developer masuk ke fase "Green"
dimana developer mengembangkan kode implementasi fitur sehingga _test case_ berhasil/_pass_.

Fase terakhir, yaitu fase "Refactor", bertujuan untuk meningkatkan kualitas kode
serta menjaga agar _test case_-_test case_ yang sudah ada tidak kembali gagal/_fail_.

Untuk memberikan gambaran lebih konkrit mengenai fase-fase TDD,
mari coba berlatih mengimplementasikan sebuah fitur sederhana dengan gaya TDD,
yaitu fitur "Visitor Counter" yang akan menghitung dan menampilkan jumlah pengunjung yang pernah membuka aplikasi.

Buat _branch_ baru untuk keperluan pengerjaan latihan TDD.
Misalnya, buat _branch_ baru bernama `tdd` dengan perintah Git berikut:

```shell
git checkout -b tdd
# Alternatif perintah Git jika menggunakan program Git versi terkini:
git switch -c tdd
```
Kemudian ikuti instruksi pada latihan masing-masing fase berikut secara berurutan.

### Latihan: Fase "Red"

Misalnya kita punya deskripsi fitur "View Counter" dalam gaya _user story_ sebagai berikut:
"_As a user, I would like to see how frequent the app has been visited._"
Kita dapat membayangkan bahwa pengguna perlu dapat melihat jumlah kunjungan pada aplikasi.
Untuk keperluan pelatihan ini, mari coba implementasikan secara sederhana saja,
yaitu dengan memunculkan sebuah pesan berisi angka kunjungan di halaman Todo List
dan jumlah data kunjungan tidak perlu disimpan ke dalam _database_.

Pertama, buka _class_ Java `TodoListControllerTest` untuk mulai menambahkan _test case_ baru,
yaitu _test case_ yang membuktikan bahwa jumlah kunjungan akan dilampirkan ke dalam _template_ HTML halaman Todo List.
_Test case_ dituliskan sebagai _method_ baru bernama `showList_countFirstVisit` sebagai berikut:

```java
@Test
@DisplayName("First visit to /list should produce a correct string in the HTML page")
void showList_countFirstVisit() throws Exception {
    mockMvc.perform(get("/list"))
           .andExpectAll(
               status().isOk(),
               content().contentTypeCompatibleWith(TEXT_HTML),
               content().encoding(UTF_8),
               content().string(containsString("This list has been viewed 1 time"))
           );
}
```

Kemudian coba jalankan _test case_ baru tersebut dari dalam IDE dengan menekan tombol di samping baris deklarasi _test case_ tersebut,
seperti yang digambarkan pada _screenshot_ berikut:

![Contoh menjalankan sebuah test case di IDE](https://pmpl.cs.ui.ac.id/workshops/images/day_1_sqa_-_run_test_case_ide.png)

Hasil dari _test case_ tersebut pasti akan gagal/_fail_.
Jangan khawatir karena fase "Red" pada TDD memang mengharapkan awal dari implementasi setiap fitur harus gagal terlebih dahulu.
Developer kemudian akan mengimplementasikan fitur dengan benar di fase berikutnya ("Green") sehingga lulus/_pass_ setiap _test case_ yang ada.

Silakan simpan terlebih dahulu hasil pekerjaan dengan membuat _commit_ Git.
Pesan _commit_ bisa disesuaikan agar menunjukkan bahwa saat ini anda baru saja menyelesaikan fase "Red" TDD,
misal: "_[RED] Write test for displaying visit counter_"

### Latihan: Fase "Green"

Sebelum mulai membuat implementasi dengan benar,
mari pikirkan sebuah solusi paling sederhana yang akan meluluskan _test case_ di atas.
Jika mengacu pada kode _test_ saat ini,
_test case_ di atas akan melakukan verifikasi pada halaman HTML dengan mencari kemunculan teks "_This list has been viewed 1 time_".
Supaya _test case_ dapat lulus, maka salah satu solusi paling sederhana
(dan juga paling naif) adalah menambahkan teks yang diharapkan di dalam dokumen _template_ HTML yang akan dikirimkan sebagai HTTP Response.

Buka berkas HTML bernama `list.html` dan tambahkan kode HTML berikut di antara penutup _tag_ `</form>` dan penutup _tag_ `</div>`:

```html
<footer>
    <p class="text-muted" id="view_count">This list has been viewed 1 time.</p>
</footer>
```

Kemudian jalankan kembali _test case_ dan lihat hasilnya.
Hasilnya pasti akan lulus/_pass_.

Secara TDD, solusi naif di atas memang sah tapi hanya berlaku ketika halaman Todo List hanya dikunjungi satu kali.
Oleh karena itu, mari coba menambahkan _test case_ baru yang menyimulasikan ketika halaman Todo List sudah pernah dibuka dua kali.
Buka kembali _class_ Java `TodoListControllerTest` dan tambahkan _method_ baru berikut sebagai _test case_ kunjungan dilakukan dua kali:

```java
@Test
@DisplayName("Second visit to /list should produce a correct string in the HTML page")
void showList_countSecondVisit() throws Exception {
    mockMvc.perform(get("/list"))
           .andExpectAll(
               status().isOk(),
               content().contentTypeCompatibleWith(TEXT_HTML),
               content().encoding(UTF_8),
               content().string(containsString("This list has been viewed 1 time"))
           );

    mockMvc.perform(get("/list"))
           .andExpectAll(
               status().isOk(),
               content().contentTypeCompatibleWith(TEXT_HTML),
               content().encoding(UTF_8),
               content().string(containsString("This list has been viewed 2 time"))
           );
}
```

Jalankan _test case_ baru tersebut.
Hasilnya pasti gagal, karena halaman HTML saat ini hanya mengandung "_This list has been viewed 1 time_".

Untuk membuat _test case_ baru tersebut lulus,
maka solusi naif yang bisa diikuti adalah menambahkan teks yang diharapkan di dalam dokumen _template_ HTML.
Tapi pikirkan kembali, apakah mau membuat implementasi fitur hingga tuntas dengan cara tersebut?
Oleh karena itu, mari ubah kembali dokumen HTML sehingga pesan jumlah kunjungannya dihasilkan secara dinamis berdasarkan perhitungan yang dilakukan oleh _backend_ aplikasi.
Silakan ubah kembali dokumen HTML `list.html` dengan mengganti isi _tag_ `<p>` dengan sintaks Thymeleaf
sehingga teks yang ditampilkan mengandung nominal angka kunjungan yang dihitung dari _backend_.

<details>
<summary>Contoh kode HTML versi akhirnya dapat dilihat pada potongan kode berikut:</summary>

```html
<footer>
    <p class="text-muted" id="view_count" th:if="${viewCount}"
       th:text="'This list has been viewed ' + ${viewCount} + ' time(s)'">
        This list has been viewed 0 time(s).
    </p>
</footer>
```
</details>

Jalankan kembali _test case_.
Hasilnya akan kembali gagal karena implementasi saat ini mengharapkan nilai `viewCount` dari _backend_ aplikasi.

Sekarang buatlah sebuah _class_ Java baru bernama `ViewCounterService` di dalam _package_ `com.example.sitodo.service`.
Objek dari _class_ Java ini akan berperan sebagai _service_ yang menyediakan implementasi _business logic_ perhitungan kunjungan.
Isi dari _class_ Java tersebut dapat mengikuti kode berikut:

```java
package com.example.sitodo.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ViewCounterService {

    private final AtomicInteger viewCount = new AtomicInteger(0);

    public int incrementAndGet() {
        return viewCount.incrementAndGet();
    }
}
```

Setelah membuat _service class_ baru, buka _class_ Java `TodoListController`
dan tambahkan _field_ yang akan menampung objek _service_ baru tersebut.
Sebagai contoh, potongan kode berikut menambahkan _field_ baru bernama `viewCounterService`
dan _setter method_ terkait dengan konfigurasi _dependency injection_ melalui _setter method_:

```java
private ViewCounterService viewCounterService;

@Autowired
public void setViewCounterService(ViewCounterService viewCounterService) {
    this.viewCounterService = viewCounterService;
}
```

Kemudian perbaharui implementasi _method_ `showList` yang menerima URL tanpa parameter dan dengan parameter.
Tambahkan _statement_ untuk menambahkan atribut `viewCount` yang mengandung perhitungan jumlah kunjungan.
Contoh kodenya adalah sebagai berikut:

```java
@GetMapping("/list")
public String showList(TodoList todoList, Model model) {
    model.addAttribute("todoList", todoList);
    model.addAttribute("motivationMessage", todoListService.computeMotivationMessage(todoList));
    model.addAttribute("viewCount", viewCounterService.incrementAndGet());

    return "list";
}

@GetMapping("/list/{id}")
public String showList(@PathVariable("id") Long id, Model model) {
    TodoList foundTodoList = todoListService.getTodoListById(id);

    model.addAttribute("todoList", foundTodoList);
    model.addAttribute("motivationMessage", todoListService.computeMotivationMessage(foundTodoList));
    model.addAttribute("viewCount", viewCounterService.incrementAndGet());

    return "list";
}
```

Sekarang jalankan _test case_ yang telah dibuat sebelumnya.
Hasilnya akan kembali gagal karena `TodoListController` membutuhkan objek `ViewCounterService` ketika test berjalan.
Oleh karena itu, buka kembali berkas `TodoListControllerTest` dan tambahkan _mock object_ `ViewCounterService` sebagai _field_:

```java
@MockBean
private ViewCounterService viewCounterService;
```

Kemudian perbaharui instruksi pada _test case_ agar menyimulasikan perilaku `ViewCounterService` seakan-akan melakukan perhitungan kunjungan.
Tambahkan potongan kode berikut sebelum memanggil objek `mockMvc` di dalam _method_ `showList_countFirstVisit`:

```java
when(viewCounterService.incrementAndGet()).thenReturn(1);
```

Lalu tambahkan kode serupa di dalam _method_ `showList_countSecondVisit`:

```java
// Sebelum pemanggilan mockMvc pertama
when(viewCounterService.incrementAndGet()).thenReturn(1);

// Sebelum pemanggilan mockMvc kedua
when(viewCounterService.incrementAndGet()).thenReturn(2);
```

Versi akhir _method_ `showList_countFirstVisit` dan `showList_countSecondVisit` adalah sebagai berikut:

```java
@Test
@DisplayName("First visit to /list should produce a correct string in the HTML page")
void showList_countFirstVisit() throws Exception {
    when(viewCounterService.incrementAndGet()).thenReturn(1);
    mockMvc.perform(get("/list"))
           .andExpectAll(
               status().isOk(),
               content().contentTypeCompatibleWith(TEXT_HTML),
               content().encoding(UTF_8),
               content().string(containsString("This list has been viewed 1 time"))
           );
}

@Test
@DisplayName("Second visit to /list should produce a correct string in the HTML page")
void showList_countSecondVisit() throws Exception {
    when(viewCounterService.incrementAndGet()).thenReturn(1);
    mockMvc.perform(get("/list"))
           .andExpectAll(
               status().isOk(),
               content().contentTypeCompatibleWith(TEXT_HTML),
               content().encoding(UTF_8),
               content().string(containsString("This list has been viewed 1 time"))
           );

    when(viewCounterService.incrementAndGet()).thenReturn(2);
    mockMvc.perform(get("/list"))
           .andExpectAll(
               status().isOk(),
               content().contentTypeCompatibleWith(TEXT_HTML),
               content().encoding(UTF_8),
               content().string(containsString("This list has been viewed 2 time"))
           );
}
```

Jalankan kembali _test case_ yang menyimulasikan satu kali dan dua kali kunjungan.
Hasilnya akan lulus/_pass_.
Simpan hasil pekerjaan dengan membuat _commit_ Git dengan pesan _commit_ seperti `[GREEN] Implement visit counter`,
lalu _push_ ke repositori Git anda di GitHub. Kemudian buatlah Pull Request untuk menggabungkan _branch_ `tdd` ke _branch_ utama (`main`) dan tunggu GitHub Actions selesai. Jika GitHub Actions sukses, maka silakan _merge_.

### Fase "Refactor"

Setelah menuntaskan fase "Red" dan "Green", developer dapat memasuki fase "Refactor" untuk memperbaiki desain kode yang telah dibuat.
Fase "Refactor" mengacu pada kegiatan _refactoring_ yang bertujuan untuk meningkatkan kualitas kode tanpa merusak kebenaran kode tersebut.
Fase ini dibutuhkan karena proses implementasi yang dilakukan pada fase "Green" seringkali fokus untuk meluluskan _test_ dengan cepat
sehingga kode yang dituliskan mungkin dibuat tanpa mempertimbangkan aspek kualitas.

Potensi perbaikan yang dapat dilakukan bisa diidentifikasi dari ada tidaknya _code smells_ pada kode.
Proses identifikasi dapat dilakukan oleh developer berdasarkan pengalaman dan pengetahuan,
ataupun dibantu dengan _tools_ seperti SonarCloud yang dapat menganalisis kualitas kode.

Contoh-contoh _code smells_ yang umum ditemukan antara lain:

- Duplikasi pada kode, seperti ada kumpulan _statement_ identik yang dituliskan berulang kali di beberapa _method_ dalam sebuah _class_ Java.
- Penamaan yang kurang deskriptif, seperti memberikan nama variabel dengan nama yang tidak bermakna.
- Penerapan praktik yang sudah kuno, seperti menggunakan `@RequestMapping` pada _controller_ Spring Boot.

## Analisis & Laporan Kualitas Kode

Sebagai bagian dari penjaminan kualitas, alur CI/CD dapat dirancang agar menjalankan serangkaian program secara otomatis setiap kali ada perubahan pada kode proyek.
Salah satu kasus yang dapat diantisipasi dengan adanya alur CI/CD adalah melakukan mitigasi terhadap regresi pada aplikasi.
Eksekusi _test_ secara otomatis dapat membuktikan bahwa perubahan terbaru pada kode proyek tidak merusak kondisi kode saat ini.
Namun tentu saja mitigasi terhadap regresi baru bisa tercapai jika kode proyek memang mengandung _test_ dengan cakupan yang menyeluruh
dan kode _test_ memang dituliskan dengan tujuan untuk menjamin kebenaran implementasi.

Salah satu tolok ukur "cakupan pengujian" yang umum digunakan di industri adalah _line coverage_,
yaitu persentase baris atau _statement_ kode proyek yang telah dijalankan oleh _test suite_.
Jika melihat pada nilai _line coverage_ kode proyek Sitodo versi _upstream_, saat ini _line coverage_ bernilai 97%.
Artinya adalah 97% _statement_ kode pada proyek sudah pernah dijalankan oleh _test suite_.

Tolok ukur alternatif yang sering digunakan selain _line coverage_ adalah _branch coverage_,
yaitu persentase percabangan alur program pada kode proyek yang telah dijalankan oleh _test suite_.

Kualitas kode proyek juga dapat dianalisis dari bagaimana kode proyek dituliskan.
Analisis kualitas kode dapat dilakukan melalui _tool_ yang didukung oleh SonarSource, yaitu [SonarScanner](https://docs.sonarsource.com/sonarqube/latest/analyzing-source-code/scanners/sonarscanner/).
Cara kerja SonarScanner adalah sebagai berikut:

1. SonarScanner melakukan analisis terhadap kode program
   dan mengumpulkan laporan-laporan dari _test framework_
   serta hasil analisis dari _tools_ lain yang didukung oleh SonarCloud.
2. Hasil analisis SonarScanner dan laporan-laporan lainnya diagregatkan sebelum dikirim ke SonarCloud.
3. SonarCloud menerima laporan agregat dari SonarScanner
   dan mengolahnya untuk menentukan kualitas kode.
4. Hasil pengolahan SonarCloud akan ditampilkan di laman proyek analisis terkait.

### Latihan: Membersihkan Beberapa Code Smells

Lihat koleksi _code smells_ yang telah diidentifikasi dan dilaporkan oleh proyek analisis di SonarCloud.
Kemudian, pilih minimal satu buah _code smells_ dan coba perbaiki _code smells_ tersebut.
Mulai dengan membuat _branch_ `code-smell` terlebih dahulu.
Kemudian, untuk setiap perbaikan sebuah _code smell_, simpan pekerjaan anda sebagai _commit_ Git dengan pesan _commit_ yang mencantumkan nama _code smell_ terkait.

Berikut ini ada beberapa kategori _code smells_ yang dapat diperbaiki,
terurut berdasarkan tingkat kesulitan dan dapat diselesaikan dalam waktu singkat:

- _Remove this `public` modifier_ - menghapus _visibility modifier_ `public` pada _class_ Java yang tidak akan digunakan dari luar modul
- _Declare this local variable with `var` instead_ - mengganti deklarasi variabel bertipe data dengan `var`, yaitu sintaks Java baru yang muncul sejak Java versi 10.
- _Define a constant instead of duplicating this literal string `n` times_ - membuat sebuah variabel konstan berisi string yang akan digunakan berulang kali
- _This `for` loop can be replaced by a `foreach` loop_ - mengganti blok perulangan `for` menjadi _enhanced-`for`_ di Java

Jika sudah selesai, silakan _push_ ke GitHub dan buat Pull Request ke _branch_ utama.
Pastikan GitHub Actions sudah sukses terlebih dahulu sebelum _merge_.

## Penutup Sesi Pagi (SQA, TDD)

Anda sudah mencoba secara garis besar penerapan TDD
dan aktivitas penjaminan kualitas dengan bantuan _tools_.
Sebelum mengakhiri workshop, jangan lupa menyimpan hasil pekerjaan sebagai _commit_ Git dan _push_ ke _fork_.

Untuk bahan diskusi saat refleksi:

- Apa saja kendala Bapak/Ibu dalam pekerjaan saat ini yang mungkin dapat diperbaiki dengan menerapkan materi yang dipelajari hari ini?
- Jika sudah pernah menerapkan _testing_, apa kendala Bapak/Ibu saat ini dalam membuat kode test dan menjalankan _test suite_?
- Setelah melihat contoh-contoh _code smells_ yang diidentifikasi oleh SonarCloud, apakah semua _code smells_ perlu ditindaklanjuti?

Selanjutnya kita akan masuk ke materi mengenai BDD.

## Functional Tests & BDD

Anda telah diperkenalkan dengan contoh kode test dari tingkat _unit test_ hingga _functional test_.
_Unit test_ menguji komponen software terkecil ("unit", seperti fungsi dan _method_) secara independen dan, idealnya, terisolasi dari komponen lainnya.
Sedangkan _functional test_ menguji software secara utuh dengan menyimulasikan interaksi pengguna ketika menggunakan software.

Hari ini kita akan melihat bagaimana _functional test_ dapat dikembangkan lebih lanjut untuk mendukung proses pengembangan software dengan gaya Behavior-Driven Development (BDD).
Sebagai contoh, berikut ini adalah potongak kode sebuah _test case_ dari _functional test suite_ Sitodo yang menyimulasikan aksi menambahkan todo item baru:

```java
@Test
@DisplayName("A user can create a single todo item")
void addTodoItem_single() {
    open("/");
    checkOverallPageLayout();

    // Create a new item
    postNewTodoItem("Buy milk");

    // See the list for the newly inserted item
    checkItemsInList(List.of("Buy milk"));

    // The page can be accessed at the new, unique URL
    String currentUrl = webdriver().driver().url();
    assertTrue(currentUrl.matches(".+/list/\\d+$"), "The URL was: " + currentUrl);
}
```

Dapat dilihat bahwa alur uji coba dituliskan secara imperatif oleh developer.
Developer mengimplementasikan serangkaian instruksi kepada "aktor" (dalam hal ini, browser yang dikendalikan Selenium WebDriver)
untuk dapat menyimulasikan alur penggunaan sebuah fitur pada software yang sedang diujicobakan (System Under Test, disingkat SUT).
Kumpulan instruksi tersebut seringkali sangat teknis dan sulit untuk dipahami oleh anggota tim selain developer,
terutama anggota tim yang fokus pada aspek bisnis (_requirements_) dan penjaminan mutu.

Walaupun developer bisa meningkatkan _readability_ kode dengan memberikan nama _test case_ secara deskriptif
serta menuliskan kode dengan gaya penulisan mengikuti bahasa natural (seringkali disebut sebagai _fluent-style_),
_functional test_ masih terpisah dari kebutuhan (_requirements_) pengembangan sehingga tidak ada keterkaitan (_traceability_)
antara _requirements_ dengan _test_.

BDD hadir untuk berusaha menjembatani tiga peran dalam pengembangan: bisnis, tim pengembang, dan penjaminan mutu.
Ketiga peran tersebut berkolaborasi dalam proses pengembangan dengan menyusun _requirements_ yang _traceable_ dari deskripsi, implementasi, dan uji cobanya.

## Menulis Deskripsi Fitur & Skenario Test Dengan Gherkin

Ingat kembali bahwa salah satu tujuan BDD adalah ingin menjembatani peran non-teknis (misal: _client_, analis) dengan peran teknis (misal: tim pengembang).
Peran non-teknis akan berkolaborasi lebih erat dengan peran teknis selama proses pengembangan berlangsung.
Bentuk kolaborasi lebih erat ini salah satunya dituangkan dalam bentuk keterlibatan peran non-teknis dalam membuat deskripsi fitur serta menyusun skenario test fitur tersebut.

Peran non-teknis dapat membuat deskripsi fitur dan skenario test dengan menggunakan bahasa Gherkin.
Gherkin adalah bahasa dengan struktur yang sederhana dan dituliskan menggunakan sintaks bahasa natural.
Deskripsi fitur dan skenario test dapat dituliskan menggunakan Gherkin seperti menuliskan prosa dalam bahasa natural.
Tentu saja ada aturan sintaks dan struktur yang harus diikuti agar dapat dijalankan oleh _test framework_.

Sebagai contoh, mari lihat potongan contoh skenario test berikut yang dapat ditemukan di `src/test/resources/features/todo/add_todo.feature`:

```gherkin
Feature: Add items into the todo list
    As a user, I would like to add todo items.

    Scenario: Add single item into the list
        Given Alice is looking at the list
        When she adds "Touch grass" to the list
        Then she sees "Touch grass" as an item in the list
```

Skenario test dituliskan sebagai bagian dari deskripsi fitur yang dituliskan dalam berkas dengan ekstensi `.feature`.
Pada contoh di atas, deskripsi fitur dimulai dengan teks `Feature: Add items into the todo list` yang berarti berkas ini merupakan deskripsi fitur "Add items into the todo list".
Selanjutnya, terdapat teks bebas yang menjelaskan lebih rinci fitur terkait.
Teks bebas tersebut dapat juga berisi dengan deskripsi _use case_ atau _user story_.

Sebuah fitur dapat memiliki satu atau lebih skenario test.
Pada contoh di atas, saat ini hanya ada sebuah skenario test.
Skenario test berisi tiga buah komponen penting di dalam membuat instruksi pengujian dengan gaya BDD: `Given`, `When`, dan `Then`.
`Given` merepresentasikan kondisi awal skenario test sebelum menjalankan aksi aktor.
`When` merepresentasikan aksi aktor dalam SUT.
`Then` merepresentasikan kondisi akhir skenario test setelah menjalankan aksi aktor.

> Catatan: Apabila anda butuh mendeskripsikan lebih dari satu aksi atau kondisi,
> maka kondisi atau aksi tersebut dapat dikonjungsikan menggunakan kata kunci `And`.

Berikut ini adalah daftar referensi terkait sintaks Gherkin yang direkomendasikan:

- [Gherkin Syntax - Cucumber Documentation](https://cucumber.io/docs/gherkin/)

### Latihan: Menulis Skenario Test Baru

Mari coba membuat sebuah deskripsi fitur beserta sebuah skenario test baru.
Silakan ikuti langkah-langkah berikut:

1. [ ] Buat _branch_ baru bernama `bdd-scenario`
2. [ ] Buka berkas `add_todo.feature` dan lengkapi deskripsi fitur untuk menyimulasikan aksi menambahkan dua buah todo items ke dalam todo list. Tulis skenarionya mengikuti kerangka `Given`, `When`, dan `Then`.

<details>

<summary>Salah satu contoh solusi dari latihan mandiri dapat dilihat pada contoh kode berikut:</summary>

```gherkin
Feature: Add items into the todo list
    As a user, I would like to add todo items.

    Scenario: Add single item into the list
        Given Alice is looking at the list
        When she adds "Touch grass" to the list
        Then she sees "Touch grass" as an item in the list

    Scenario: Add two items into the list
        Given Alice is looking at the list
        When she adds "Touch grass" to the list
        And she adds "Buy milk" to the list
        Then she sees "Touch grass" as an item in the list
        And she sees "Buy milk" as an item in the list
```
</details>

Apabila telah selesai, simpan hasil pekerjaan sebagai _commit_ Git.
Kemudian _push_ ke repositori Git anda di GitHub.

## Menjalankan Test Suite BDD

_Test suite_ BDD dapat dijalankan dengan dua cara
yaitu menggunakan IDE (IntelliJ IDEA) atau menggunakan Maven di _shell_.
Jika anda ingin menjalankan _test suite_ BDD menggunakan IDE,
maka buka berkas _class_ Java yang merepresentasikan _test suite_ BDD,
yaitu _class_ [`CucumberTestSuite`][CucumberTestSuite] di IDE.
Kemudian jalankan _class_ tersebut dengan menekan tombol yang muncul di sisi kiri editor pada IDE
dan pilih _Run 'CucumberTestSuite'_ seperti yang tergambarkan di _screenshot_ berikut:

![CucumberTestSuite di IDE](https://pmpl.cs.ui.ac.id/workshops/images/day_3_-_cucumber_test_suite.png)

Jika nyaman menggunakan _shell_ atau ingin menjalankan _test suite_ BDD di lingkungan _Continuous Integration_ (CI),
panggil perintah Maven berikut di _shell_:

```shell
mvn -P bdd clean verify
```

_Test suite_ BDD akan dijalankan oleh _test runner_ secara _headless_ terhadap aplikasi yang berjalan secara lokal.
Begitu _test suite_ BDD selesai dijalankan, laporan hasil test dapat ditemukan di folder `target/site/serenity`.
Apabila menggunakan Maven, perintah `mvn verify` juga akan menghasilkan laporan komplit hasil test sebagai dokumen HTML.
Contoh tampilan laporannya dapat dilihat pada _screenshot_ berikut:

![Contoh laporan BDD Serenity](https://pmpl.cs.ui.ac.id/workshops/images/day_3_-_serenity_report.png)

Deskripsi fitur memang bermanfaat bagi peran non-teknis agar dapat ikut berkontribusi di dalam proses pengembangan.
Namun bukan berarti deskripsi fitur dan skenario test secara ajaib bisa otomatis menguji coba SUT sesuai keinginan.
Peran teknis seperti programmer dan _test engineer_ penting untuk dapat menyambungkan deskripsi fitur ke kode test.

> Catatan: Salah satu masalah yang umum terjadi ketika melakukan uji coba fungsionalitas melalui simulasi interaksi
> pada _user interface_ adalah hasil test yang tidak stabil. Ada kemungkinan skenario uji coba yang dijalankan oleh
> kode test tidak berhasil berinteraksi dengan elemen _user interface_ yang diinginkan.
> Pada aplikasi Web, isu elemen _user interface_ yang gagal ditemukan atau tidak bisa diujicobakan oleh kode test
> seringkali terjadi karena DOM Tree dari tampilan HTML belum sinkron. Hal ini sangat mungkin terjadi pada aplikasi
> Web dengan _user interface_ yang dibuat atau diubah oleh kode JavaScript.

### Latihan: Atur Konfigurasi CI/CD Untuk Menjalankan Test Suite BDD

Saat ini konfigurasi CI/CD masih mengarahkan eksekusi _test suite_ BDD ke aplikasi Sitodo versi _upstream_.
Ubah berkas `bdd.yml` di dalam folder `.github/workflows`,
lalu ganti URL aplikasi Sitodo di dalam pemanggilan perintah `sed` dengan URL ke aplikasi Sitodo versi anda.
Setelah itu, simpan perubahan sebagai _commit_ Git baru dan _push_ ke GitHub.

## Membuat Step Definition & Glue Code

Mari lihat kembali contoh skenario test "Add single item into the list":

```gherkin
Scenario: Add single item into the list
    Given Alice is looking at the list
    When she adds "Touch grass" to the list
    Then she sees "Touch grass" as an item in the list
```

Masing-masing kalimat yang diawali dengan kata kunci `Given`, `When`, dan `Then` perlu dipasangkan dengan serangkaian instruksi bahasa pemrograman.
Instruksi-instruksi tersebut seringkali disebut sebagai _step definitions_ yang merupakan bagian dari _Glue Code_,
yaitu kode program yang mendukung dan menjalankan _test suite_ BDD.

Sebagai contoh, mari lihat _step definition_ padanan kalimat berawalan `Given`:

```java
@Given("{actor} is looking at the list")
public void actor_is_looking_at_her_todo_list(Actor actor) {
    actor.wasAbleTo(NavigateTo.theTodoListPage());
}
```

_Step definition_ diimplementasikan sebagai fungsi dengan informasi tambahan yang akan memetakan fungsi tersebut dengan kalimat terkait.
Pada contoh di atas, fungsi diberikan anotasi `@Given` dengan parameter berisi teks _regular expression_ (_regex_) yang akan dicocokkan dengan kalimat di skenario test.

Apabila kalimat pada skenario test mengandung elemen yang dapat berubah-ubah seperti aktor/pengguna fitur atau input,
maka teks _regex_ dapat mengandung _placeholder_ yang akan menjadi parameter bagi fungsi _step definition_.
Pada contoh di atas, `actor` merupakan tipe parameter khusus yang disediakan templat kode untuk memetakan aktor di skenario test dengan objek `Actor` yang akan dikendalikan oleh implementasi fungsi _step definition_.

Inti utama dari fungsi _step definition_ adalah objek `Actor`, yaitu representasi dari aktor/pengguna fitur.
Pada contoh di atas, objek `Actor` memanggil fungsi `wasAbleTo` yang menyimulasikan aksi-aksi aktor untuk mencapai kondisi awal yang diinginkan.
Fungsi `wasAbleTo` menerima parameter berupa satu atau lebih objek `Performable` yang merepresentasikan aksi fisik ketika berinteraksi dengan SUT.

Mari lihat _step definition_ padanan kalimat berawalan `When`:

```java
@When("{actor} adds {string} to the list")
public void she_adds_to_the_list(Actor actor, String itemName) {
    actor.attemptsTo(AddAnItem.withName(itemName));
}
```

Pada contoh di atas, objek `Actor` memanggil fungsi `attemptsTo` yang kurang lebih serupa dengan fungsi `wasAbleTo` di contoh sebelumnya.
Fungsi `attemptsTo` sama-sama menerima satu atau lebih objek `Performable`.

Hasil akhir dari ketiga aksi di atas akan diverifikasi oleh fungsi _step definition_ yang dipetakan dengan kalimat `Then`.
Contohnya adalah sebagai berikut:

```java
@Then("{actor} sees {string} as an item in the list")
public void she_sees_as_an_item_in_the_todo_list(Actor actor, String expectedItemName) {
    List<String> todoItems = TodoListPage.ITEMS_LIST.resolveAllFor(actor)
                                                    .textContents();

    actor.attemptsTo(
        Ensure.that(todoItems)
              .contains(expectedItemName)
    );
}
```

Pada contoh kode di atas, objek `Actor` sama-sama menggunakan fungsi `attemptsTo` seperti pada fungsi _step definition_ untuk kalimat `When`.
Hanya saja, parameter fungsi tersebut sebaiknya diisi dengan objek `PerformablePredicate` yang dapat dibuatkan oleh objek `Ensure`.
Objek `Ensure` menyediakan fungsi untuk merepresentasikan aksi verifikasi yang dilakukan oleh aktor terhadap kondisi akhir SUT.

Jika aksi verifikasi berhasil dilakukan, maka _test framework_ akan melaporkan skenario test tersebut berhasil/"pass".
Sebaliknya, jika ternyata verifikasi gagal dilakukan, maka _test framework_ akan melaporkan skenario test tersebut gagal/"fail".

Sebagai ringkasan, resep yang umum dipakai dalam membuat _step definition_ adalah sebagai berikut:

- _Step definition_ dari kalimat berawalan `Given` berperan untuk menyiapkan kondisi awal pada SUT sebelum menyimulasikan fitur.
  Kode Java yang dapat digunakan dalam implementasi _step definition_ ini adalah fungsi `wasAbleTo` milik objek `Actor`.
  Fungsi tersebut menerima parameter berupa satu atau lebih objek `Performable` yang merepresentasikan aksi aktor.
  Masing-masing objek `Performable` akan dijalankan sesuai dengan urutan objeknya di dalam parameter fungsi `wasAbleTo`.
- _Step definition_ dari kalimat berawalan `When` berperan untuk menjalankan aksi-aksi utama yang menyimulasikan fitur.
  Kode Java yang dapat digunakan dalam implementasi _step definition_ ini adalah fungsi `attemptsTo` milik objek `Actor`.
  Serupa dengan `wasAbleTo`, fungsi `attemptsTo` menerima parameter berupa satu atau lebih objek `Performable`.
- _Step definition_ dari kalimat berawalan `Then` berperan untuk melakukan verifikasi terhadap kondisi akhir pada SUT dengan kondisi yang diharapkan oleh skenario test.
  Kode Java yang dapat digunakan dalam implementasi _step definition_ ini adalah fungsi `attemptsTo` milik objek `Actor`.
  Hanya saja, objek-objek yang dimasukkan ke parameter fungsi `attemptsTo` sebaiknya adalah objek bertipe `PerformablePredicate` yang dapat dibangun dengan bantuan objek `Ensure`.
  Objek `Ensure` mengandung instruksi yang melakukan verifikasi terhadap kondisi akhir SUT.

## Latihan: Membuat Test Suite BDD Untuk Fitur Lainnya

Anda telah melihat beberapa contoh _test scenario_ dan _glue code_.
Pada latihan terakhir hari ini, silakan coba buat _test scenario_ baru beserta _glue code_ yang dibutuhkan.
Sebagai contoh, saat ini belum ada pengujian terhadap kasus sebuah todo item dilabelkan dari Finished menjadi Not Finished.
Contoh lain skenario yang belum diujicobakan adalah melakukan verifikasi terhadap pesan motivasi yang muncul.
Silakan dikerjakan secara mandiri dan boleh berdiskusi dengan peserta pelatihan lainnya.
Instruktur pelatihan juga tersedia untuk menanggapi pertanyaan.

Jika sudah selesai, simpan hasil pekerjaan sebagai _commit_ Git.
Kemudian _push_ ke _branch_ `bdd`, dan buat Pull Request untuk _merge_ ke _branch_ utama.

## Penutup Sesi Siang (BDD)

Selamat! Anda telah mencapai penghujung pelatihan mengenai SQA dan penerapan contoh BDD.

Untuk bahan diskusi saat refleksi:

- [ ] Apa perbedaan _test_ tradisional (yaitu, _test_ yang tidak memiliki keterkaitan dengan _requirements_)
  dengan _test_ BDD?
- [ ] Apakah hasil eksekusi _test suite_ BDD anda stabil?
- [ ] Apa saja isu yang dapat terjadi ketika menguji SUT melalui simulasi interaksi terhadap _user interface_?
- [ ] Apakah BDD bisa diterapkan juga untuk menguji _software_ dengan _user interface_ non-visual seperti API?

## Atribusi & Lisensi

Instruksi pelatihan di dalam proyek ini mengadopsi materi pelatihan internal mengenai SQA
dan BDD yang diadakan di Fakultas Ilmu Komputer Universitas Indonesia. Sumber acuan dapat
dilihat di tautan berikut: [Workshop SQA, TDD, BDD](https://pmpl.cs.ui.ac.id/workshops/)

Proyek ini menggunakan lisensi [MIT](./LICENSE).
