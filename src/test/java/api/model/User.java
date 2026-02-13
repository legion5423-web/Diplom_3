package api.model;

public class User {
    private String email;
    private String password;
    private String name;

    // Конструктор без параметров (нужен для сериализации)
    public User() {
    }

    // Конструктор со всеми параметрами
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // Конструктор для логина (только email и password)
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Геттеры и сеттеры
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}