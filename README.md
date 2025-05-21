# 🔐 Java Password Manager

A simple console-based Password Manager written in Java using AES encryption. It securely stores passwords for different services in a local JSON file and encrypts them using a generated secret key.

---

## 🚀 Features

- AES encryption for passwords (using Java Crypto)
- Local storage using `passwords.json`
- Secret key saved in `key.key`
- Simple command-line interface
- No external database required

---

## 🛠 Requirements

- Java 8 or higher
- JSON library (org.json)
  - You can add it via Maven or manually download the `.jar` file from [Maven Central](https://mvnrepository.com/artifact/org.json/json)

---

## 🔧 Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/PasswordManagerJava.git
   cd PasswordManagerJava
   ```

2. Compile the Java file:
   ```bash
   javac -cp .:json.jar PasswordManager.java
   ```

3. Run the program:
   ```bash
   java -cp .:json.jar PasswordManager
   ```

> 📝 Replace `json.jar` with the actual path to the `org.json` library if needed.

---

## 🔒 Security Notes

- `key.key` contains the secret AES key and should never be shared or uploaded to GitHub.
- `passwords.json` stores encrypted passwords, but should also be kept private.

Make sure both are listed in `.gitignore`.

---

## 📂 Example File Structure

```
PasswordManagerJava/
│
├── PasswordManager.java
├── key.key              (generated after first run)
├── passwords.json       (created automatically)
├── .gitignore
└── README.md
```

---

## 🧾 License

MIT License
