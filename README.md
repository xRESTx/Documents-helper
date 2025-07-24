<details>
<summary><strong>🇺🇸 English</strong></summary>

# Documents Helper
Desktop application for batch document processing:  
fill Word templates, convert DOCX/XLSX → PDF, process Excel, translate Armenian → Russian.

> ⭐ If this project saves you time, **give it a star on GitHub** and share it with friends!  
> 👍 Your likes help the repo grow and keep the updates coming.

---

## 🚀 Features
| Function | Description |
| --- | --- |
| **Templates** | Fill placeholders in DOCX with data and get ready invoices / acts in Russian & English |
| **DOCX → PDF** | One-click conversion of any Word / Excel file to PDF |
| **Excel → PDF** | Batch convert selected Excel workbooks to PDF with fit-to-page |
| **Translations** | Translate Armenian text inside any Excel sheet to Russian via MyMemory API |

---

## 📁 Project Structure
```
documents-helper
├── src/main/java/org/example
│   ├── convert/WordToPdf.java            # PowerShell-based DOCX → PDF
│   ├── convert/ExcelToPdf.java           # PowerShell-based XLSX → PDF
│   ├── logic/WordTemplateProcessor.java  # Apache POI template filler
│   ├── translation/ExcelTranslator.java  # hy → ru translator
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
or the fat-jar:
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

### 1. Fill Templates
1. Open the **“Сформировать счет”** tab.
2. Enter data (date, invoice #, hours, quantity, total).
3. Click **Сохранить** → `output_ru.docx`, `output_en.docx` and their PDF versions appear in `output/`.

### 2. DOCX → PDF
1. Switch to **“Из Word в PDF”**.
2. Select source file & destination folder.
3. (Optional) specify PDF name → **Конвертировать**.

### 3. Excel → PDF (batch)
1. Switch to **“Из Excel в PDF”**.
2. **Выбрать Excel файлы** – multi-select supported.
3. **Конвертировать** – progress bar & log show status.

### 4. Translation (hy → ru)
1. Switch to **“Переводы”**.
2. Pick an Excel file (`.xls` or `.xlsx`).
3. Choose output folder & file name → **Перевести**.  
   Each string cell is translated from **Armenian to Russian** via MyMemory API.

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
- **Apache POI 5.2.3** – DOCX/XLSX manipulation
- **PowerShell COM** – DOCX/XLSX → PDF conversion
- **MyMemory API** – free Armenian → Russian translation
- **Gradle Shadow** – fat-jar
- **jpackage** – native installer

---

## 🚧 Roadmap
- [ ] Add more translation directions (DeepL / Google)
- [ ] Internationalize UI (EN / RU / AM)
- [ ] Settings pane (API keys, default folders)

</details>

<details open>
<summary><strong>🇷🇺 Русский</strong></summary>

# Documents Helper
Настольное приложение для массовой обработки документов:  
заполнение шаблонов Word, конвертация DOCX/XLSX → PDF, работа с Excel, перевод армянского → русского.

> ⭐ Если проект сэкономил вам время — поставьте **звёздочку на GitHub** и поделитесь с друзьями!  
> 👍 Ваши лайки помогают развивать репозиторий и добавлять новые функции.

---

## 🚀 Возможности
| Функция | Описание |
| --- | --- |
| **Шаблоны** | Заполнение DOCX-шаблонов данными и получение готовых счетов/актов на русском и английском |
| **DOCX → PDF** | Однокнопочная конвертация любых Word/Excel-файлов в PDF |
| **Excel → PDF** | Пакетная конвертация выбранных Excel-файлов в PDF с подгонкой по странице |
| **Переводы** | Перевод армянского текста внутри любого Excel-листа на русский через MyMemory API |

---

## 📁 Структура проекта
```
documents-helper
├── src/main/java/org/example
│   ├── convert/WordToPdf.java            # Конвертер DOCX → PDF на PowerShell
│   ├── convert/ExcelToPdf.java           # Конвертер XLSX → PDF на PowerShell
│   ├── logic/WordTemplateProcessor.java  # Заполнение шаблонов Apache POI
│   ├── translation/ExcelTranslator.java  # Переводчик hy → ru
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

### 1. Заполнение шаблонов
1. Откройте вкладку **«Сформировать счет»**.
2. Введите данные (дата, номер счёта, часы, количество, итог).
3. Нажмите **«Сохранить»** → в папке `output/` появятся `output_ru.docx`, `output_en.docx` и их PDF-версии.

### 2. DOCX → PDF
1. Перейдите во вкладку **«Из Word в PDF»**.
2. Выберите исходный файл и папку для сохранения.
3. (Опционально) задайте имя PDF → **«Конвертировать»**.

### 3. Excel → PDF (пакетно)
1. Перейдите во вкладку **«Из Excel в PDF»**.
2. **Выбрать Excel файлы** — поддерживается множественный выбор.
3. **Конвертировать** — индикатор прогресса и лог покажут статус.

### 4. Перевод (hy → ru)
1. Откройте вкладку **«Переводы»**.
2. Выберите Excel-файл (`.xls` или `.xlsx`).
3. Укажите папку и имя выходного файла → **Перевести**.  
   Каждая текстовая ячейка переводится с **армянского на русский** через MyMemory API.

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
- **Apache POI 5.2.3** – работа с DOCX/XLSX
- **PowerShell COM** – конвертация DOCX/XLSX → PDF
- **MyMemory API** – бесплатный перевод армянского → русский
- **Gradle Shadow** – fat-jar
- **jpackage** – нативный установщик

---

## 🚧 Планы
- [ ] Добавить другие направления перевода (DeepL / Google)
- [ ] Интернационализация интерфейса (EN / RU / AM)
- [ ] Панель настроек (API ключи, папки по умолчанию)

</details>