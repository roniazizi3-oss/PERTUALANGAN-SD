package com.example.data.local

import com.example.data.model.*

object InitialDataGenerator {

    fun getDefaultProfile(): UserProfileEntity {
        return UserProfileEntity(
            id = 1,
            name = "Budi Pratama",
            gradeLevel = 3,
            schoolName = "SD Negeri Nusantara 01",
            coins = 420,
            exp = 1350,
            currentStreak = 5,
            lastActiveDate = "2026-09-29",
            avatarCharacter = "kancil",
            avatarHat = "wisuda",
            avatarOutfit = "seragam_sd",
            avatarAccessory = "kacamata_bintang",
            unlockedItems = "kancil,owl,wisuda,mahkota,seragam_sd,jas_peneliti,kacamata_bintang"
        )
    }

    fun getInitialProgress(): List<SubjectProgressEntity> {
        val list = mutableListOf<SubjectProgressEntity>()
        val subjects = listOf("MATH", "BAHASA", "SCIENCE")

        for (grade in 1..6) {
            for (subj in subjects) {
                for (level in 1..5) {
                    val title = when (subj) {
                        "MATH" -> when (level) {
                            1 -> "Dasar Berhitung Pulau Angka"
                            2 -> "Teka-teki Tambah Kurang"
                            3 -> "Jalur Pecahan Ceria"
                            4 -> "Taman Geometri Misterius"
                            else -> "Puncak Master Matematika"
                        }
                        "BAHASA" -> when (level) {
                            1 -> "Lembah Kata Bergambar"
                            2 -> "Hutan Membaca Cerita"
                            3 -> "Gua Ejaan & Tanda Baca"
                            4 -> "Sungai Antonim & Sinonim"
                            else -> "Kastil Penulis Cilik"
                        }
                        else -> when (level) {
                            1 -> "Hutan Panca Indera & Tubuh"
                            2 -> "Pulau Hewan & Tumbuhan"
                            3 -> "Laboratorium Energi Alam"
                            4 -> "Observatorium Tata Surya"
                            else -> "Benteng Ekosistem Bumi"
                        }
                    }

                    // Level 1 of grade 1 to 3 are unlocked by default, grade 3 level 1-2 completed
                    val isUnlocked = level == 1 || (grade == 3 && level <= 3) || (grade < 3)
                    val isCompleted = (grade < 3 && level <= 3) || (grade == 3 && level <= 2)
                    val stars = if (isCompleted) 3 else 0
                    val score = if (isCompleted) 100 else 0

                    list.add(
                        SubjectProgressEntity(
                            gradeLevel = grade,
                            subject = subj,
                            levelNumber = level,
                            title = title,
                            starsEarned = stars,
                            isUnlocked = isUnlocked,
                            isCompleted = isCompleted,
                            highScore = score,
                            totalQuestions = 4
                        )
                    )
                }
            }
        }
        return list
    }

    fun getInitialBadges(): List<AchievementBadgeEntity> {
        return listOf(
            AchievementBadgeEntity(
                badgeKey = "streak_master",
                title = "Pejuang Rajin",
                description = "Belajar 5 hari berturut-turut tanpa absen!",
                category = "STREAK",
                iconName = "LocalFireDepartment",
                currentProgress = 5,
                targetProgress = 5,
                isUnlocked = true,
                unlockedDate = "Kemarin"
            ),
            AchievementBadgeEntity(
                badgeKey = "math_genius",
                title = "Juara Berhitung",
                description = "Selesaikan 10 level matematika dengan nilai sempurna",
                category = "BELAJAR",
                iconName = "Calculate",
                currentProgress = 6,
                targetProgress = 10,
                isUnlocked = false
            ),
            AchievementBadgeEntity(
                badgeKey = "science_explorer",
                title = "Peneliti Cilik",
                description = "Pahami materi sains tentang makhluk hidup dan tata surya",
                category = "BELAJAR",
                iconName = "Science",
                currentProgress = 4,
                targetProgress = 5,
                isUnlocked = false
            ),
            AchievementBadgeEntity(
                badgeKey = "bookworm",
                title = "Kutu Buku Nusantara",
                description = "Selesaikan 15 bacaan cerita bahasa Indonesia",
                category = "BELAJAR",
                iconName = "MenuBook",
                currentProgress = 15,
                targetProgress = 15,
                isUnlocked = true,
                unlockedDate = "3 hari lalu"
            ),
            AchievementBadgeEntity(
                badgeKey = "avatar_fashion",
                title = "Gaya Petualang",
                description = "Buka 5 kostum dan aksesoris unik di lemari avatar",
                category = "AVATAR",
                iconName = "Checkroom",
                currentProgress = 7,
                targetProgress = 5,
                isUnlocked = true,
                unlockedDate = "Hari ini"
            ),
            AchievementBadgeEntity(
                badgeKey = "team_player",
                title = "Sahabat Belajar",
                description = "Bantu teman di ruang diskusi kelompok 3 kali",
                category = "BELAJAR",
                iconName = "Groups",
                currentProgress = 2,
                targetProgress = 3,
                isUnlocked = false
            )
        )
    }

    fun getInitialTasks(): List<ClassroomTaskEntity> {
        return listOf(
            ClassroomTaskEntity(
                id = "gc_task_1",
                courseName = "Matematika Kelas 3A - Bu Ratna",
                teacherName = "Ibu Ratna Kumalasari, S.Pd",
                title = "Tugas Pecahan Sederhana & Luas Bangun",
                description = "Selesaikan latihan 4 soal pecahan senilai di aplikasi Petualang SD dan kirim nilai otomatis ke Google Classroom.",
                subject = "MATH",
                gradeLevel = 3,
                dueDate = "Besok, 17:00 WIB",
                isSubmitted = true,
                score = 95,
                isSyncedWithClassroom = true
            ),
            ClassroomTaskEntity(
                id = "gc_task_2",
                courseName = "IPA & Lingkungan - Pak Hendra",
                teacherName = "Bapak Hendra Wijaya, M.Pd",
                title = "Kuis Metamorfosis Kupu-Kupu & Katak",
                description = "Ikuti petualangan level Sains pulau metamorfosis untuk memahami daur hidup serangga dan amfibi.",
                subject = "SCIENCE",
                gradeLevel = 3,
                dueDate = "30 September 2026, 20:00",
                isSubmitted = false,
                score = null,
                isSyncedWithClassroom = true
            ),
            ClassroomTaskEntity(
                id = "gc_task_3",
                courseName = "Bahasa Indonesia - Bu Siti",
                teacherName = "Ibu Siti Rahmawati, S.Pd",
                title = "Mencari Antonim dan Sinonim Kata",
                description = "Mainkan petualangan Lembah Kata untuk menemukan pasangan sinonim kata gembira, cerdik, dan gigih.",
                subject = "BAHASA",
                gradeLevel = 3,
                dueDate = "2 Oktober 2026, 12:00",
                isSubmitted = false,
                score = null,
                isSyncedWithClassroom = true
            )
        )
    }

    fun getInitialDiscussions(): List<GroupDiscussionEntity> {
        return listOf(
            GroupDiscussionEntity(
                id = 1,
                authorName = "Siti Aisyah",
                authorAvatar = "owl",
                gradeLevel = 3,
                subjectTag = "Matematika",
                questionTitle = "Bagaimana cara mudah menghafal perkalian 7 dan 8?",
                messageText = "Hai teman-teman, aku sering bingung perkalian 7 x 8. Ada tips seru atau trik cepat berhitung tidak?",
                replyCount = 4,
                likesCount = 9,
                timeAgo = "15 menit lalu",
                isSolved = true
            ),
            GroupDiscussionEntity(
                id = 2,
                authorName = "Rian Hidayat",
                authorAvatar = "lion",
                gradeLevel = 3,
                subjectTag = "Sains",
                questionTitle = "Mengapa tanaman putri malu menutup saat disentuh?",
                messageText = "Tadi di sekolah ada tanaman putri malu di halaman. Saat kusentuh daunnya langsung menguncup, apa penyebabnya ya?",
                replyCount = 3,
                likesCount = 6,
                timeAgo = "1 jam lalu",
                isSolved = true
            ),
            GroupDiscussionEntity(
                id = 3,
                authorName = "Dewi Lestari",
                authorAvatar = "cat",
                gradeLevel = 3,
                subjectTag = "Bahasa Indonesia",
                questionTitle = "Perbedaan kata depan 'di' dan awalan 'di-'",
                messageText = "Kapan huruf 'di' ditulis dipisah dengan spasi dan kapan digabung jadi satu kata? Masih suka terbalik saat nulis cerita.",
                replyCount = 5,
                likesCount = 12,
                timeAgo = "3 jam lalu",
                isSolved = false
            )
        )
    }

    fun getInitialReports(): List<StudentReportEntity> {
        return listOf(
            StudentReportEntity(
                studentName = "Budi Pratama",
                gradeLevel = 3,
                dateString = "28 September 2026",
                subject = "Matematika",
                scorePercentage = 95,
                correctCount = 19,
                totalCount = 20,
                durationSeconds = 480,
                strongTopics = "Perkalian Dasar, Nilai Tempat",
                weakTopics = "Soal Cerita Pembagian"
            ),
            StudentReportEntity(
                studentName = "Budi Pratama",
                gradeLevel = 3,
                dateString = "27 September 2026",
                subject = "Sains",
                scorePercentage = 90,
                correctCount = 18,
                totalCount = 20,
                durationSeconds = 420,
                strongTopics = "Ciri Makhluk Hidup, Panca Indera",
                weakTopics = "Perubahan Wujud Benda"
            ),
            StudentReportEntity(
                studentName = "Budi Pratama",
                gradeLevel = 3,
                dateString = "26 September 2026",
                subject = "Bahasa Indonesia",
                scorePercentage = 100,
                correctCount = 20,
                totalCount = 20,
                durationSeconds = 360,
                strongTopics = "Ide Pokok, Sinonim Kata",
                weakTopics = "-"
            )
        )
    }

    fun getCurriculumQuestions(): List<QuizQuestionEntity> {
        val q = mutableListOf<QuizQuestionEntity>()

        // ================= KELAS 1 =================
        // Math Level 1
        q.add(QuizQuestionEntity(0, 1, "MATH", 1, "Budi punya 3 buah apel merah 🍎, lalu Ibu memberi lagi 2 buah apel 🍎. Berapa jumlah apel Budi sekarang?", "4", "5", "6", "7", 1, "3 + 2 = 5 buah apel.", "Hitung satu per satu: 1, 2, 3, ditambah 2 lagi."))
        q.add(QuizQuestionEntity(0, 1, "MATH", 1, "Manakah angka yang lebih besar dari 7?", "5", "6", "9", "4", 2, "Angka 9 bernilai lebih besar dari angka 7.", "Cari angka di urutan setelah angka 7."))
        q.add(QuizQuestionEntity(0, 1, "MATH", 1, "Bentuk bangun datar yang menyerupai uang koin adalah...", "Segitiga", "Lingkaran", "Persegi", "Kotak", 1, "Uang koin berbentuk bulat lingkaran.", "Koin tidak memiliki sudut."))
        q.add(QuizQuestionEntity(0, 1, "MATH", 1, "Hasil dari 10 - 4 adalah...", "5", "6", "7", "8", 1, "10 dikurangi 4 sama dengan 6.", "Bila ada 10 jari, tutup 4 jari."))

        // Bahasa Level 1
        q.add(QuizQuestionEntity(0, 1, "BAHASA", 1, "Manakah huruf berikut yang merupakan huruf vokal?", "B", "M", "A", "T", 2, "Huruf vokal adalah A, I, U, E, O.", "Ingat bunyi a-i-u-e-o."))
        q.add(QuizQuestionEntity(0, 1, "BAHASA", 1, "Jika kita dibantu oleh teman, ucapan yang benar adalah...", "Maaf", "Terima kasih", "Sampai jumpa", "Permisi", 1, "Kita wajib mengucapkan terima kasih saat menerima bantuan.", "Ungkapan rasa bersyukur dan menghargai."))
        q.add(QuizQuestionEntity(0, 1, "BAHASA", 1, "Kata 'buku' terdiri dari ... suku kata.", "1", "2", "3", "4", 1, "'bu - ku' memiliki 2 suku kata.", "Tepuk tangan saat mengeja: bu-ku."))
        q.add(QuizQuestionEntity(0, 1, "BAHASA", 1, "Lawan kata (antonim) dari 'besar' adalah...", "Tinggi", "Kecil", "Panjang", "Lebar", 1, "Lawan kata besar adalah kecil.", "Ukuran yang mini."))

        // Science Level 1
        q.add(QuizQuestionEntity(0, 1, "SCIENCE", 1, "Bagian tubuh yang kita gunakan untuk melihat benda di sekitar adalah...", "Telinga", "Hidung", "Mata", "Lidah", 2, "Mata adalah indera penglihatan.", "Ada dua di wajah kita untuk memandang warna."))
        q.add(QuizQuestionEntity(0, 1, "SCIENCE", 1, "Benda yang tampak bersinar terang dan hangat di langit pada siang hari adalah...", "Bulan", "Bintang", "Matahari", "Pelangi", 2, "Matahari terbit di pagi hari dan menyinari bumi.", "Sumber cahaya terbesar di siang hari."))
        q.add(QuizQuestionEntity(0, 1, "SCIENCE", 1, "Manakah contoh benda hidup berikut ini?", "Batu", "Kucing", "Meja", "Pensil", 1, "Kucing adalah makhluk hidup karena bernapas dan bergerak.", "Makhluk yang membutuhkan makan dan minum."))
        q.add(QuizQuestionEntity(0, 1, "SCIENCE", 1, "Indera pengecap rasa manis dan asin pada makanan adalah...", "Lidah", "Kulit", "Telinga", "Hidung", 0, "Lidah dapat merasakan manis, asin, asam, dan pahit.", "Ada di dalam mulut."))

        // ================= KELAS 2 =================
        q.add(QuizQuestionEntity(0, 2, "MATH", 1, "Berapa hasil dari 45 + 32?", "75", "77", "87", "67", 1, "45 + 32 = 77.", "Jumlahkan satuan (5+2=7), lalu puluhan (4+3=7)."))
        q.add(QuizQuestionEntity(0, 2, "MATH", 1, "Pada bilangan 284, angka 8 menempati nilai tempat...", "Ratusan", "Puluhan", "Satuan", "Ribuan", 1, "Angka 2 adalah ratusan, 8 adalah puluhan, 4 adalah satuan.", "Perhatikan posisi tengah."))
        q.add(QuizQuestionEntity(0, 2, "MATH", 1, "1 meter sama dengan berapa sentimeter (cm)?", "10 cm", "50 cm", "100 cm", "1000 cm", 2, "1 meter = 100 sentimeter.", "M ke CM turun dua tangga dikali 100."))
        q.add(QuizQuestionEntity(0, 2, "MATH", 1, "Jarum panjang jam menunjuk ke angka 12, jarum pendek ke angka 4. Waktu menunjukkan pukul...", "04.00", "12.00", "04.12", "12.04", 0, "Jarum pendek menunjuk jam, jarum panjang menunjuk menit.", "Tepat jam 4."))

        q.add(QuizQuestionEntity(0, 2, "BAHASA", 1, "Penulisan huruf kapital yang benar pada nama kota adalah...", "jakarta", "Jakarta", "jaKarta", "JAKARTA", 1, "Huruf awal nama kota dan tempat harus huruf kapital.", "Huruf depannya saja yang besar."))
        q.add(QuizQuestionEntity(0, 2, "BAHASA", 1, "Kata tanya yang tepat untuk menanyakan tempat kejadian adalah...", "Kapan", "Siapa", "Di mana", "Berapa", 2, "Kata 'di mana' digunakan untuk menanyakan lokasi atau tempat.", "Menanyakan letak posisi."))
        q.add(QuizQuestionEntity(0, 2, "BAHASA", 1, "Tanda baca yang dipakai di akhir kalimat berita adalah...", "Tanda tanya (?)", "Tanda seru (!)", "Tanda titik (.)", "Tanda koma (,)", 2, "Kalimat berita diakhiri dengan tanda titik.", "Titik menandakan kalimat selesai."))
        q.add(QuizQuestionEntity(0, 2, "BAHASA", 1, "Tokoh hewan yang terkenal cerdik dalam dongeng Indonesia adalah...", "Buaya", "Kancil", "Harimau", "Gajah", 1, "Kancil terkenal memiliki banyak akal cerdik.", "Hewan berkaki empat yang lincah."))

        q.add(QuizQuestionEntity(0, 2, "SCIENCE", 1, "Bagian tanaman yang berada di dalam tanah dan menyerap air adalah...", "Daun", "Batang", "Akar", "Bunga", 2, "Akar berfungsi menyerap air dan zat hara dari dalam tanah.", "Berada paling bawah pohon."))
        q.add(QuizQuestionEntity(0, 2, "SCIENCE", 1, "Air yang dimasukkan ke dalam freezer kulkas akan mengalami proses...", "Mencair", "Membeku", "Menguap", "Mengembun", 1, "Air cair menjadi es padat disebut membeku.", "Suhu dingin mengubah air jadi es."))
        q.add(QuizQuestionEntity(0, 2, "SCIENCE", 1, "Hewan pemakan tumbuhan (rumput dan dedaunan) disebut...", "Karnivora", "Herbivora", "Omnivora", "Insektivora", 1, "Herbivora adalah golongan hewan pemakan tumbuhan (seperti sapi & kambing).", "Herba artinya tanaman/rumput."))
        q.add(QuizQuestionEntity(0, 2, "SCIENCE", 1, "Tempat tinggal dan berkembang biak alami bagi makhluk hidup dinamakan...", "Kandang", "Habitat", "Taman", "Pasar", 1, "Habitat adalah lingkungan alami tempat hidup satwa dan tumbuhan.", "Istilah biologi lingkungan."))

        // ================= KELAS 3 =================
        q.add(QuizQuestionEntity(0, 3, "MATH", 1, "Berapakah hasil dari 7 x 8?", "54", "56", "64", "48", 1, "7 dikali 8 adalah 56.", "7 x 7 = 49, ditambah 7 = 56."))
        q.add(QuizQuestionEntity(0, 3, "MATH", 1, "Sebuah kue dipotong menjadi 4 bagian sama besar. Budi memakan 1 potong. Berapa pecahan kue yang dimakan Budi?", "1/2", "1/4", "3/4", "2/4", 1, "1 bagian dari total 4 bagian sama dengan 1/4.", "Satu per empat."))
        q.add(QuizQuestionEntity(0, 3, "MATH", 1, "Keliling persegi yang memiliki panjang sisi 6 cm adalah...", "12 cm", "18 cm", "24 cm", "36 cm", 2, "Keliling persegi = 4 x sisi = 4 x 6 = 24 cm.", "Jumlahkan keempat sisinya (6+6+6+6)."))
        q.add(QuizQuestionEntity(0, 3, "MATH", 1, "Hasil pembagian dari 72 : 9 adalah...", "7", "8", "9", "6", 1, "9 x 8 = 72, sehingga 72 : 9 = 8.", "Kebalikan dari perkalian 9."))

        q.add(QuizQuestionEntity(0, 3, "BAHASA", 1, "Gagasan pokok atau masalah inti yang dibahas dalam suatu paragraf disebut...", "Kalimat penjelas", "Ide pokok", "Judul buku", "Daftar isi", 1, "Ide pokok adalah inti sari pembahasan dalam suatu paragraf.", "Pikiran utama paragraf."))
        q.add(QuizQuestionEntity(0, 3, "BAHASA", 1, "Persamaan makna kata (sinonim) dari kata 'pandai' adalah...", "Malas", "Pintar", "Giat", "Ramah", 1, "Pandai bersinonim dengan pintar atau cerdas.", "Kata yang artinya sama dengan cerdas."))
        q.add(QuizQuestionEntity(0, 3, "BAHASA", 1, "Kalimat berikut yang menunjukkan kalimat ajakan adalah...", "Ayo kita jaga kebersihan kelas bersama!", "Siapa yang piket hari ini?", "Dilarang membuang sampah!", "Budi membaca buku di perpustakaan.", 0, "Kata 'Ayo' dan 'Mari' merupakan ciri khas kalimat ajakan.", "Diawali kata ayo."))
        q.add(QuizQuestionEntity(0, 3, "BAHASA", 1, "Lawan kata (antonim) dari kata 'hemat' adalah...", "Pelit", "Boros", "Rajin", "Cermat", 1, "Lawan kata hemat adalah boros (suka membuang-buang uang/barang).", "Suka menghambur-hamburkan uang."))

        q.add(QuizQuestionEntity(0, 3, "SCIENCE", 1, "Urutan tahapan metamorfosis sempurna pada kupu-kupu yang benar adalah...", "Telur -> Kepompong -> Ulat -> Kupu-kupu", "Telur -> Ulat -> Kepompong -> Kupu-kupu", "Ulat -> Telur -> Kupu-kupu -> Kepompong", "Kepompong -> Ulat -> Telur -> Kupu-kupu", 1, "Telur menetas menjadi ulat (larva), berubah jadi kepompong (pupa), lalu jadi kupu-kupu.", "Dimulai dari telur lalu ulat makan daun."))
        q.add(QuizQuestionEntity(0, 3, "SCIENCE", 1, "Berikut ini yang merupakan ciri-ciri makhluk hidup adalah...", "Dapat berpindah sendiri dan bernapas", "Berwarna mengkilap", "Tidak memerlukan makanan", "Bentuknya selalu tetap", 0, "Makhluk hidup bernapas, makan, tumbuh, dan berkembang biak.", "Perlu udara dan nutrisi."))
        q.add(QuizQuestionEntity(0, 3, "SCIENCE", 1, "Perubahan wujud benda dari cair menjadi gas saat air mendidih disebut...", "Membeku", "Mencair", "Menguap", "Mengembun", 2, "Air yang dipanaskan sampai mendidih akan berubah menjadi uap air.", "Uap asap putih naik ke atas."))
        q.add(QuizQuestionEntity(0, 3, "SCIENCE", 1, "Sumber energi utama dan terbesar bagi seluruh kehidupan di bumi adalah...", "Minyak bumi", "Matahari", "Batu bara", "Listrik", 1, "Matahari memberikan energi panas dan cahaya untuk fotosintesis tumbuhan dan kehidupan bumi.", "Bintang di pusat tata surya."))

        // ================= KELAS 4 =================
        q.add(QuizQuestionEntity(0, 4, "MATH", 1, "KPK (Kelipatan Persekutuan Terkecil) dari 4 dan 6 adalah...", "12", "18", "24", "8", 0, "Kelipatan 4: 4, 8, 12, 16. Kelipatan 6: 6, 12, 18. Yang sama dan terkecil adalah 12.", "Cari angka kelipatan terkecil yang bisa dibagi 4 dan 6."))
        q.add(QuizQuestionEntity(0, 4, "MATH", 1, "Bentuk pecahan desimal dari 3/4 adalah...", "0,34", "0,50", "0,75", "0,25", 2, "3 dibagi 4 = 0,75.", "Setengah adalah 0,5, tiga perempat adalah 0,75."))
        q.add(QuizQuestionEntity(0, 4, "MATH", 1, "Sudut yang besarnya tepat 90 derajat dinamakan sudut...", "Lancip", "Tumpul", "Siku-siku", "Lurus", 2, "Sudut 90° adalah sudut siku-siku (tegak lurus).", "Seperti pojok buku tulis."))
        q.add(QuizQuestionEntity(0, 4, "MATH", 1, "Luas persegi panjang dengan panjang 12 cm dan lebar 5 cm adalah...", "34 cm²", "60 cm²", "17 cm²", "120 cm²", 1, "Luas = panjang x lebar = 12 x 5 = 60 cm².", "Kalikan panjang dengan lebarnya."))

        q.add(QuizQuestionEntity(0, 4, "BAHASA", 1, "Kalimat yang memerlukan objek penderita disebut kalimat...", "Transitif", "Intransitif", "Tanya", "Perintah", 0, "Kalimat transitif membutuhkan objek (contoh: 'Ibu memasak nasi').", "Memiliki Subjek, Predikat, dan Objek."))
        q.add(QuizQuestionEntity(0, 4, "BAHASA", 1, "Gaya bahasa yang membandingkan benda mati seolah-olah bernyawa disebut majas...", "Hiperbola", "Metafora", "Personifikasi", "Asosiasi", 2, "Personifikasi menganggap benda mati bertindak seperti manusia (contoh: 'Nyiur melambai-lambai').", "Dari kata person (manusia)."))
        q.add(QuizQuestionEntity(0, 4, "BAHASA", 1, "Teks yang menggambarkan suatu objek secara rinci sehingga pembaca seolah merasakan langsung adalah...", "Teks Narasi", "Teks Deskripsi", "Teks Prosedur", "Teks Eksplanasi", 1, "Teks deskripsi melukiskan rupa, suasana, atau bentuk secara mendalam.", "Menggambarkan objek detail."))
        q.add(QuizQuestionEntity(0, 4, "BAHASA", 1, "Arti peribahasa 'Ada gula ada semut' adalah...", "Banyak orang suka makan yang manis", "Di mana ada rezeki atau kesenangan, di situ banyak orang berkumpul", "Semut selalu mencari makanan di dapur", "Kita harus berhati-hati menyimpan makanan", 1, "Orang-orang akan berdatangan ke tempat yang memberikan keuntungan atau rezeki.", "Kiasan rezeki dan keramaian."))

        q.add(QuizQuestionEntity(0, 4, "SCIENCE", 1, "Bagian bunga yang berfungsi sebagai alat kelamin jantan adalah...", "Putik", "Benang sari", "Mahkota", "Kelopak", 1, "Benang sari menghasilkan serbuk sari (sel kelamin jantan).", "Penghasil serbuk sari."))
        q.add(QuizQuestionEntity(0, 4, "SCIENCE", 1, "Dalam rantai makanan di sawah, posisi padi adalah sebagai...", "Konsumen tingkat 1", "Produsen", "Pengurai", "Predator", 1, "Tumbuhan hijau mampu membuat makanan sendiri lewat fotosintesis (produsen).", "Penghasil makanan pertama."))
        q.add(QuizQuestionEntity(0, 4, "SCIENCE", 1, "Gaya tarik yang menyebabkan buah apel jatuh ke tanah adalah gaya...", "Magnet", "Gesek", "Gravitasi bumi", "Otot", 2, "Gravitasi bumi menarik semua benda bermassa menuju pusat bumi.", "Gaya tarik bumi."))
        q.add(QuizQuestionEntity(0, 4, "SCIENCE", 1, "Sumber energi ramah lingkungan yang tidak akan pernah habis disebut energi...", "Fosil", "Terbarukan", "Nuklir", "Kimia", 1, "Contoh energi terbarukan: sinar matahari, angin, dan aliran air.", "Dapat diperbarui terus menerus."))

        // ================= KELAS 5 =================
        q.add(QuizQuestionEntity(0, 5, "MATH", 1, "Hasil dari 1/2 + 1/4 adalah...", "2/6", "3/4", "2/4", "4/4", 1, "Samakan penyebut: 2/4 + 1/4 = 3/4.", "Ubah 1/2 menjadi 2/4."))
        q.add(QuizQuestionEntity(0, 5, "MATH", 1, "Sebuah peta menggunakan skala 1 : 100.000. Jarak pada peta 3 cm. Jarak sebenarnya adalah...", "3 km", "30 km", "300 meter", "30.000 cm", 0, "3 x 100.000 cm = 300.000 cm = 3 kilometer.", "300.000 cm dibagi 100.000 = 3 km."))
        q.add(QuizQuestionEntity(0, 5, "MATH", 1, "Sebuah mobil melaju menempuh jarak 120 km selama 2 jam. Kecepatan rata-ratanya adalah...", "50 km/jam", "60 km/jam", "70 km/jam", "80 km/jam", 1, "Kecepatan = Jarak / Waktu = 120 / 2 = 60 km/jam.", "Bagi jarak dengan waktu tempuh."))
        q.add(QuizQuestionEntity(0, 5, "MATH", 1, "Volume sebuah kubus dengan rusuk panjang 5 cm adalah...", "25 cm³", "100 cm³", "125 cm³", "150 cm³", 2, "Volume kubus = s x s x s = 5 x 5 x 5 = 125 cm³.", "Lima pangkat tiga."))

        q.add(QuizQuestionEntity(0, 5, "BAHASA", 1, "Pantun terdiri dari 4 baris. Baris 1 dan 2 disebut...", "Isi", "Sampiran", "Syair", "Amanat", 1, "Baris 1 & 2 adalah sampiran, baris 3 & 4 adalah isi pantun.", "Pengantar rima pantun."))
        q.add(QuizQuestionEntity(0, 5, "BAHASA", 1, "Manakah kalimat berikut yang merupakan sebuah FAKTA?", "Gunung Bromo terlihat sangat indah sekali", "Indonesia terletak di kawasan Asia Tenggara", "Belajar matematika lebih menyenangkan dari bahasa", "Makanan khas Padang paling lezat di dunia", 1, "Letak geografis Indonesia di Asia Tenggara adalah fakta ilmiah yang terbukti pasti.", "Pernyataan yang dapat dibuktikan secara nyata."))
        q.add(QuizQuestionEntity(0, 5, "BAHASA", 1, "Teks yang menerangkan proses terjadinya suatu fenomena alam atau sosial disebut teks...", "Eksposisi", "Eksplanasi", "Fabel", "Anekdot", 1, "Teks eksplanasi menjelaskan hubungan sebab-akibat suatu fenomena.", "Menjelaskan 'mengapa' dan 'bagaimana'."))
        q.add(QuizQuestionEntity(0, 5, "BAHASA", 1, "Rima sajak akhir pada pantun yang baku berskema...", "a - a - a - a", "a - b - a - b", "a - a - b - b", "a - b - b - a", 1, "Sajak pantun bersilang a-b-a-b.", "Rima baris 1 sama dengan 3, rima baris 2 sama dengan 4."))

        q.add(QuizQuestionEntity(0, 5, "SCIENCE", 1, "Organ utama dalam sistem pernapasan manusia yang berfungsi menyerap oksigen adalah...", "Jantung", "Paru-paru", "Lambung", "Hati", 1, "Paru-paru menukar oksigen dan karbon dioksida di dalam alveolus.", "Organ bernapas dalam dada."))
        q.add(QuizQuestionEntity(0, 5, "SCIENCE", 1, "Perpindahan kalor (panas) secara merambat melalui zat padat tanpa disertai perpindahan zatnya disebut...", "Konduksi", "Konveksi", "Radiasi", "Evaporasi", 0, "Konduksi terjadi saat sendok logam terasa panas saat mengaduk teh hangat.", "Perambatan pada logam."))
        q.add(QuizQuestionEntity(0, 5, "SCIENCE", 1, "Proses penguapan air laut menjadi awan akibat panas matahari pada siklus air dinamakan...", "Kondensasi", "Evaporasi", "Presipitasi", "Infiltrasi", 1, "Evaporasi adalah penguapan cairan oleh panas matahari.", "Air menjadi uap mengangkasa."))
        q.add(QuizQuestionEntity(0, 5, "SCIENCE", 1, "Hewan yang memiliki sistem pernapasan menggunakan insang sepanjang hidupnya adalah...", "Katak dewasa", "Ikan", "Burung", "Lumba-lumba", 1, "Ikan bernapas menyerap oksigen terlarut dalam air menggunakan insang.", "Satwa air berenang bersisik."))

        // ================= KELAS 6 =================
        q.add(QuizQuestionEntity(0, 6, "MATH", 1, "Hasil dari (-15) + 20 - (-5) adalah...", "0", "10", "20", "5", 1, "(-15) + 20 = 5; 5 - (-5) = 5 + 5 = 10.", "Kurang ketemu minus jadi tambah (+)."))
        q.add(QuizQuestionEntity(0, 6, "MATH", 1, "Sebuah lingkaran memiliki jari-jari 7 cm. Keliling lingkaran tersebut adalah... (π = 22/7)", "22 cm", "44 cm", "154 cm", "88 cm", 1, "Keliling = 2 x π x r = 2 x (22/7) x 7 = 44 cm.", "Rumus 2 * pi * r."))
        q.add(QuizQuestionEntity(0, 6, "MATH", 1, "Nilai tengah dari sekumpulan data yang telah diurutkan dinamakan...", "Mean", "Median", "Modus", "Kuartil", 1, "Median adalah nilai tengah data terurut.", "Mean adalah rata-rata, modus nilai terbanyak."))
        q.add(QuizQuestionEntity(0, 6, "MATH", 1, "Sebuah tabung memiliki jari-jari alas 7 cm dan tinggi 10 cm. Volume tabung tersebut adalah... (π = 22/7)", "770 cm³", "1.540 cm³", "3.080 cm³", "440 cm³", 1, "Volume = π x r² x t = (22/7) x 49 x 10 = 154 x 10 = 1.540 cm³.", "Luas alas lingkaran dikali tinggi tabung."))

        q.add(QuizQuestionEntity(0, 6, "BAHASA", 1, "Bagian awal dalam naskah pidato resmi yang berisi salam dan penghormatan disebut...", "Isi pidato", "Pembukaan", "Penutup", "Simpulan", 1, "Pembukaan berisi salam, puji syukur, dan salam penghormatan kepada hadirin.", "Awalan sebelum menyampaikan inti pidato."))
        q.add(QuizQuestionEntity(0, 6, "BAHASA", 1, "Arti peribahasa 'Berat sama dipikul, ringan sama dijinjing' adalah...", "Pekerjaan berat harus dihindari bersama", "Bersama-sama dalam suka maupun duka serta saling membantu", "Menimbang beban yang berat dan ringan", "Orang yang kuat menolong yang lemah saja", 1, "Maknanya gotong royong dan kebersamaan menghadapi segala situasi.", "Gotong royong bersama."))
        q.add(QuizQuestionEntity(0, 6, "BAHASA", 1, "Karya sastra berupa puisi lama yang terdiri dari 4 baris bersajak a-a-a-a dinamakan...", "Pantun", "Gurindam", "Syair", "Karmina", 2, "Syair adalah puisi lama bersajak a-a-a-a yang keempat barisnya merupakan isi.", "Semua baris berakhiran bunyi sama."))
        q.add(QuizQuestionEntity(0, 6, "BAHASA", 1, "Kalimat efektif yang benar sesuai kaidah tata bahasa Indonesia adalah...", "Bagi para siswa-siswa semua dimohon berkumpul", "Para siswa dimohon berkumpul di lapangan", "Siswa-siswa sekalian semuanya harap berkumpul", "Semua para hadirin dimohon segera datang", 1, "Menghindari pemborosan kata 'para' dan pengulangan 'siswa-siswa'.", "Cari yang tidak memboroskan kata."))

        q.add(QuizQuestionEntity(0, 6, "SCIENCE", 1, "Perkembangbiakan tumbuhan secara vegetatif buatan dengan cara mengupas kulit batang dan melapisinya dengan tanah subur disebut...", "Mencangkok", "Stek", "Okulasi", "Merunduk", 0, "Mencangkok menghasilkan tanaman baru yang cepat berbuah dan mirip induknya.", "Mengupas kulit dahan lalu dibungkus sabut."))
        q.add(QuizQuestionEntity(0, 6, "SCIENCE", 1, "Planet terbesar dalam sistem tata surya kita adalah...", "Mars", "Bumi", "Jupiter", "Saturnus", 2, "Jupiter adalah raksasa gas sekaligus planet paling besar dalam tata surya.", "Planet raksasa dengan bintik merah raksasa."))
        q.add(QuizQuestionEntity(0, 6, "SCIENCE", 1, "Bentuk adaptasi pohon jati untuk mengurangi penguapan air saat musim kemarau adalah...", "Menggugurkan daunnya", "Memanjangkan akarnya", "Mengubah daun menjadi duri", "Menutup bunganya", 0, "Pohon jati meranggas (menggugurkan daun) untuk menghemat air saat kemarau kering.", "Daunnya berguguran saat kemarau."))
        q.add(QuizQuestionEntity(0, 6, "SCIENCE", 1, "Pada rangkaian listrik seri, jika salah satu lampu padam maka lampu yang lain akan...", "Menyala lebih terang", "Ikut padam", "Tetap menyala normal", "Berkedip-kedip", 1, "Rangkaian seri hanya memiliki satu jalur arus. Jika putus di satu titik, arus berhenti mengalir ke semua lampu.", "Arus terputus serentak."))

        return q
    }
}
