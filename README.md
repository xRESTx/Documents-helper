<details>
<summary><strong>🇺🇸 English</strong></summary>

# Documents Helper
Desktop application for batch document processing: fill Word templates, convert DOCX → PDF, process Excel, translate texts.

> ⭐ If this project saves you time, **give it a star on GitHub** and share it with friends!  
> 👍 Your likes help the repo grow and keep the updates coming.

---

## 🚀 Features
| Function | Description |
| --- | --- |
| **Templates** | Fill placeholders in DOCX with data and get ready invoices / acts in Russian & English |
| **DOCX → PDF** | One-click conversion of any Word / Excel file to PDF |
| **Excel loader** | Multi-select Excel files for further processing |
| **Translations** | Stub for future translation module |

---

## 📁 Project Structure
```
documents-helper
├── src/main/java/org/example
│   ├── convert/WordToPdf.java            # PowerShell-based converter
│   ├── logic/WordTemplateProcessor.java  # Apache POI template filler
│   ├── ui/StartUI.java                   # JavaFX UI
│   └── Main.java                         # Launcher
├── templates/                            # DOCX templates (ru / en)
├── output/                               # Generated files
├── build.gradle                          # Gradle build & jpackage
└── settings.gradle
```

---

## 🛠️ Build & Run

### 1. Prerequisites
- JDK 21 (with JavaFX modules)
- Windows (conversion uses COM via PowerShell)
- Gradle 8.x

### 2. Clone & Build
```bash
git clone <repo>
cd documents-helper
gradlew shadowJar
```

### 3. Run
```bash
gradlew run
```
or use the fat-jar:
```bash
java --add-modules javafx.controls,javafx.fxml -jar build/libs/documents-helper-all-1.0.jar
```

### 4. Create Native Installer (Windows)
```bash
gradlew packageApp
```
Installer will appear in `build/dist/`.

---

## 🧪 Usage

### Fill Templates
1. Open the **“Шаблон”** tab.
2. Enter data (date, invoice #, hours, quantity, total).
3. Click **Сохранить** → `output_ru.docx`, `output_en.docx` and their PDF versions appear in `output/`.

### DOCX → PDF
1. Switch to **“Из Word в PDF”**.
2. Select source file & destination folder.
3. (Optional) specify PDF name → **Конвертировать**.

### Excel Loader
- **“Загрузка Excel”** tab allows multi-file selection for future processing.

---

## 🧩 Template Syntax
Inside any `.docx`:
```
{{date}}            → replaced with value from UI
{{invoice}}
{{hours}}
{{quantity}}
{{total}}
```
Placeholders work in paragraphs **and** tables.

---

## 📦 Tech Stack
- **Java 21** + **JavaFX 21**
- **Apache POI 5.2.3** – DOCX manipulation
- **PowerShell COM** – DOCX → PDF conversion
- **Gradle Shadow** – fat-jar
- **jpackage** – native installer

---

## 🚧 Roadmap
- [ ] Excel to PDF
- [ ] Translation module (DeepL / Google)

</details>



<details open>
<summary><strong>🇷🇺 Русский</strong></summary>

# Documents Helper
Настольное приложение для массовой обработки документов: заполнение шаблонов Word, конвертация DOCX → PDF, работа с Excel, переводы.

> ⭐ Если проект сэкономил вам время — поставьте **звёздочку на GitHub** и поделитесь с друзьями!  
> 👍 Ваши лайки помогают развивать репозиторий и добавлять новые функции.

---

## 🚀 Возможности
| Функция | Описание |
| --- | --- |
| **Шаблоны** | Заполнение DOCX-шаблонов данными и получение готовых счетов/актов на русском и английском |
| **DOCX → PDF** | Однокнопочная конвертация любых Word/Excel-файлов в PDF |
| **Загрузка Excel** | Множественный выбор Excel-файлов для последующей обработки |
| **Переводы** | Заготовка для будущего модуля перевода |

---

## 📁 Структура проекта
```
documents-helper
├── src/main/java/org/example
│   ├── convert/WordToPdf.java            # Конвертер на PowerShell
│   ├── logic/WordTemplateProcessor.java  # Заполнение шаблонов Apache POI
│   ├── ui/StartUI.java                   # JavaFX-интерфейс
│   └── Main.java                         # Точка входа
├── templates/                            # DOCX-шаблоны (ru / en)
├── output/                               # Сгенерированные файлы
├── build.gradle                          # Сборка Gradle + jpackage
└── settings.gradle
```

---

## 🛠️ Сборка и запуск

### 1. Требования
- JDK 21 (с модулями JavaFX)
- Windows (конвертация использует COM через PowerShell)
- Gradle 8.x

### 2. Клонирование и сборка
```bash
git clone <repo>
cd documents-helper
gradlew shadowJar
```

### 3. Запуск
```bash
gradlew run
```
или через fat-jar:
```bash
java --add-modules javafx.controls,javafx.fxml -jar build/libs/documents-helper-all-1.0.jar
```

### 4. Создание установщика (Windows)
```bash
gradlew packageApp
```
Установщик появится в `build/dist/`.

---

## 🧪 Использование

### Заполнение шаблонов
1. Откройте вкладку **«Шаблон»**.
2. Введите данные (дата, номер счёта, часы, количество, итог).
3. Нажмите **«Сохранить»** → в папке `output/` появятся `output_ru.docx`, `output_en.docx` и их PDF-версии.

### DOCX → PDF
1. Перейдите во вкладку **«Из Word в PDF»**.
2. Выберите исходный файл и папку для сохранения.
3. (Опционально) задайте имя PDF → **«Конвертировать»**.

### Загрузка Excel
- Вкладка **«Загрузка Excel»** позволяет множественный выбор файлов для дальнейшей обработки.

---

## 🧩 Синтаксис шаблона
Внутри любого `.docx`:
```
{{date}}            → заменится значением из интерфейса
{{invoice}}
{{hours}}
{{quantity}}
{{total}}
```
Плейсхолдеры работают в абзацах **и** таблицах.

---

## 📦 Стек технологий
- **Java 21** + **JavaFX 21**
- **Apache POI 5.2.3** – работа с DOCX
- **PowerShell COM** – конвертация DOCX → PDF
- **Gradle Shadow** – fat-jar
- **jpackage** – нативный установщик

---

## 🚧 Планы
- [ ] Конвертация Excel в PDF
- [ ] Модуль перевода (DeepL / Google)

</details>