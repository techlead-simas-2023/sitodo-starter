# Sitodo Starter Code

Contoh proyek aplikasi todo list sederhana untuk mendemonstrasikan _testing_ dari tingkat _unit_ hingga _functional_.
Proyek ini juga mengilustrasikan standar pengembangan berkualitas tinggi dengan menerapkan CI/CD dan penjaminan mutu kualitas kode.

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
Sebagai contoh, versi _upstream_ (asli) proyek ini memiliki alur CI/CD yang melaporkan hasil _test_ serta analisis kode ke tools lain bernama SonarCloud.
Hasil analisis SonarCloud versi _upstream_ dapat dilihat melalui laman SonarCloud yang dapat diakses dari kedua _badge_ berikut:

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=addianto_sitodo&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=addianto_sitodo)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=addianto_sitodo&metric=coverage)](https://sonarcloud.io/summary/new_code?id=addianto_sitodo)

Selain itu, laporan-laporan hasil _test_ juga dipublikasikan sebagai _static site_ pada GitHub Pages.
Contoh hasil _static site_ versi asli dapat dilihat di tautan berikut: [Sitodo Test Reports](https://addianto.github.io/sitodo/)

## Konfigurasi CI/CD

Untuk keperluan pelatihan hari ini, konfigurasi alur CI/CD sudah diberikan dan dapat dilihat di dalam folder `.github/workflows`.
Saat ini CI/CD telah diatur agar melakukan aksi-aksi berikut:

1. Otomasi _build_ dan _test_ pada proyek Sitodo dimana seluruh _test suite_ akan dijalankan oleh GitHub Actions (_platform_ CI/CD bawaan GitHub)
   pada _branch_ yang menjadi bagian Pull Request ke _branch_ utama (`main`).
2. Agregasi laporan-laporan _test_ dan analisis kualitas kode ke SonarCloud.
3. Publikasi laporan-laporan _test_ ke GitHub Pages.

## Lisensi

Proyek ini menggunakan lisensi [MIT](./LICENSE).
