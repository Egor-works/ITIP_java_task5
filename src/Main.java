import java.security.MessageDigest;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("№1 : \nHello there -> " + Arrays.toString(encrypt("Hello there")) + "\n[71, 30, 9, -9, 13, -17, 11, -76, 43, 26, 9, 1, -13, 7, -72] -> "
                + decrypt(new int[]{71, 30, 9, -9, 13, -17, 11, -76, 43, 26, 9, 1, -13, 7, -72}));
        System.out.println("№2 -> " + canMove("Queen", "C4", "D6"));
        System.out.println("№3 -> " + canComplete("butl", "beautiful"));
        System.out.println("№4 -> " + sumDigProd(16, 28));
        System.out.println("№5 -> " + sameVowelGroup("toe", "ocelot", "maniac"));
        System.out.println("№6 -> " + validateCard(1234567890123456L));
        System.out.println("№7 -> " + numToEng(0) + "\n\t  " + numToRus(909));
        System.out.println("№8 -> " + getSha256Hash("password123"));
        System.out.println("№9 -> " + correctTitle("jOn SnoW, kINg IN thE noRth."));
        System.out.println("№10 -> "); hexLattice(1);
    }

    /** №1 Функция, кодирующая символы введённой строки **/
    public static int[] encrypt(String string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        //закодированное представление сообщения, где нулевой элемент - числовой вид символа
        int[] code = new int[stringBuilder.length()];
        code[0] = stringBuilder.charAt(0);

        //перебор символов с расчётом отклонения от предыдущего
        for(int i = 1; i < stringBuilder.length(); i++) {
            code[i] = stringBuilder.charAt(i) - stringBuilder.charAt(i-1);
        }

        return code;
    }

    /** №1 Функция декодирующая числа из массива в символы строки **/
    public static String decrypt(int[] code) {
        StringBuilder stringBuilder = new StringBuilder();
        //декодированное сообщение, где нулевой элемент - символьное представления числа
        stringBuilder.append((char) code[0]);

        //перебор чисел с вычислением числовых значений символов
        for (int i = 1; i < code.length; i++) {
            stringBuilder.append((char) (code[i] + stringBuilder.charAt(i-1)));
        }

        return stringBuilder.toString();
    }

    /** №2 Функция, определяющая может ли фигура сделать ход **/
    public static boolean canMove(String chessman, String start, String end) {
        char[] from = start.toCharArray(); // первая клетка
        char[] to = end.toCharArray(); // вторая клетка
        // проверка допустимости введённых данных
        if(from[0] < 'A' || from[0] > 'H' || to[0] < 'A' || to[0] > 'H' ||
                 from[1] < '1' || from[1] > '8' || to[1] < '1' || to[1] > '8') return false;

        // определение возможности хода(по модулю разности клеток), в зависимости от выбора фигуры
        return switch (chessman) {
            case ("Pawn") -> ((Math.abs((int) from[0] - (int) to[0]) == 0) && (Math.abs((int) from[1] - (int) to[1]) == 1)) ||
                    ((Math.abs((int) from[0] - (int) to[0]) == 0 && Math.abs((int) from[1] - (int) to[1]) == 2) && from[1] == '2');

            case ("Knight") -> ((Math.abs((int) from[0] - (int) to[0]) == 3) || (Math.abs((int) from[1] - (int) to[1]) == 1)) &&
                    ((Math.abs((int) from[0] - (int) to[0]) == 1) || (Math.abs((int) from[1] - (int) to[1]) == 3));

            case ("Bishop") -> Math.abs((int) from[0] - (int) to[0]) == Math.abs((int) from[1] - (int) to[1]);

            case ("Rook") -> (Math.abs((int) from[0] - (int) to[0]) == 0) || (Math.abs((int) from[1] - (int) to[1]) == 0);

            case ("Queen") -> (Math.abs((int) from[0] - (int) to[0]) == Math.abs((int) from[1] - (int) to[1])) ||
                    ((Math.abs((int) from[0] - (int) to[0]) == 0) || (Math.abs((int) from[1] - (int) to[1]) == 0));

            case ("King") -> (Math.abs((int) from[0] - (int) to[0]) == 1) || (Math.abs((int) from[1] - (int) to[1]) == 1);

            default -> false;
        };
    }

    /** №3 Функция определяющая может ли слово быть завершено **/
    public static boolean canComplete(String string, String completedString) {
        int count = 0;// счётчик совпадающих символов
        //проход по всем символам законченной строки и сравнение их с символами неполной строки
        for (int i = 0; i < completedString.length(); i++) {
            if (completedString.charAt(i) == string.charAt(count)) {
                count++;
                if (string.length() == count) return true;
            }
        }
        return false;
    }

    /** №4 Функция, возвращающая произведение цифр до тех пор, пока ответ не станет длиной
     всего в 1 цифру **/
    public static int sumDigProd(int... massive) {
        // как в, четвёртом таске, только прежде суммируем все элементы массива
        int sum = Arrays.stream(massive).sum();
        while (sum > 9) {
            int temp = 1;
            while (sum > 0) {
                temp *= sum % 10;
                sum /= 10;
            }
            sum = temp;
        }
        return sum;
    }

    /** №5 Функция, которая выбирает все слова, имеющие все те же гласные (в
     любом порядке и / или количестве), что и первое слово, включая первое слово **/
    public static List<String> sameVowelGroup(String... lines) {
        // Создаём результирующий список и множество уникальных элементов
        List<String> result = new ArrayList<>();
        Set<Character> set = new HashSet<>();
        // по умолчанию вводим первое слово, вносим все его гласные в строку, гласные находим через регулярную функцию
        result.add(lines[0]);
        String word1 = lines[0].replaceAll("[^aeiouy]", "");
        // идём по всем строкам, начиная со второй, добавляем гласные в set
        for (int i = 0; i < word1.length(); i++) set.add(word1.charAt(i));

        //перебор гласных в словах и сравнение их с гласными первого слова
        for (int i = 1; i < lines.length; i++) {
            String word2 = lines[i].replaceAll("[^aeiouy]", "");
            boolean flag = true;
            for (char c : word2.toCharArray())
                if (!set.contains(c)) {
                    flag = false;
                    break;
                }
            if (flag) result.add(lines[i]);
        }
        return result;
    }

    /** №6 Функция, которая принимает число в качестве аргумента и возвращает
     true, если это число является действительным номером кредитной карты, **/
    public static boolean validateCard(long number) {
        // проверяем длину числа
        if (Math.pow(10, 20) > number && Math.pow(10, 14) <= number) return false;
        // выводим контрольную цифру из числа
        int control = (int) number % 10;
        number /= 10;
        int sum = 0;
        int k;
        //идём по числу соблюдая условия.
        for (int i = 1; Math.pow(10, i) <= number; i++) {
            //Удвойте значение каждой цифры в нечетных позициях. Если удвоенное значение имеет более 1 цифры, сложите цифры вместе
            k = (int) (number % Math.pow(10, i) / Math.pow(10, i - 1) * (i % 2 + 1));
            sum += k % 10 + k / 10;
        }
        //Вычесть последнюю цифру суммы и сравнить её с контрольной
        return 10 - sum % 10 == control;
    }

    /** №7 Функция, которая принимает положительное целое число от 0 до 999
     включительно и возвращает строковое представление этого целого числа,
     написанное на английском языке **/
    public static StringBuilder numToEng(int n) {
        StringBuilder result = new StringBuilder();
        // задаём единицы и десятки
        String[] SUBTWENTY = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
                "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
        String[] DECADES = {"twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
        // запись по формату
        if (n / 100 > 0) result.append(String.format("%s hundred ", SUBTWENTY[n / 100]));
        if (n % 100 >= 20) result.append(String.format("%s ", DECADES[n % 100 / 10]));
        if (n % 10 > 0 || n == 0) result.append(SUBTWENTY[n % 20]);
        return result;
    }

    /** №7 То же самое для русского языка **/
    public static StringBuilder numToRus(int number) {
        StringBuilder result = new StringBuilder();
        String[] SUBTWENTY = {"ноль", "один", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять",
                "десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать", "пятнадцать", "шестнадцать",
                "семнадцать", "восемнадцать", "девятнадцать"};
        String[] DECADES = {"двадцать", "тридцать", "сорок", "пятьдесят",
                "шестьдесят", "семьдесят", "восемьдесят", "девяносто"};
        String[] HUNDREDS = {"сто", "двести", "триста", "четыреста",
                "пятьсот", "шестьсот", "семьсот", "восемьсот", "девятьсот"};
        if (number / 100 > 0) result.append(String.format("%s ", HUNDREDS[number / 100 - 1]));
        if (number % 100 >= 20) result.append(String.format("%s ", DECADES[number % 100 / 10 - 2]));
        if (number % 10 > 0 || number == 0) result.append(SUBTWENTY[number % 20]);
        return result;
    }

    /** №8 Создайте функцию, которая возвращает безопасный хеш SHA-256 для данной строки **/
    public static String getSha256Hash(String line) {
        try {
            //Класс Java MessageDigest представляет криптографическую хеш-функцию
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // создаём массив битов состоящих из хэшированного представления бита числа
            byte[] hash = digest.digest(line.getBytes());
            StringBuilder hexString = new StringBuilder();
            // проходимся по массиву
            for (int i : hash) {
                //Собираем строковое представление целочисленного аргумента в виде целого числа
                String hex = Integer.toHexString(0xff & i);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /** №9 Напишите функцию, которая принимает строку и возвращает строку с правильным
     регистром для заголовков символов в серии "Игра престолов" **/
    public static String correctTitle(String text) {
        StringBuilder right = new StringBuilder(text.toLowerCase());
        //если это буква, выставляем первый символ заглавным
        right.setCharAt(0, Character.toUpperCase(text.charAt(0)));
        //проходимся по тексту, и меняем буквы, перед которыми пробел на заглавные + проверка с помощью регулярной функции, что слово не является служебной частью речи
        for (int i = 1; i < text.length(); i++)
            if (text.charAt(i - 1) == ' ' && !right.substring(i, i + 3).matches("of |in |the|and"))
                right.setCharAt(i, Character.toUpperCase(text.charAt(i)));
        return right.toString();
    }

    /** №10 Напишите функцию, которая принимает целое число n и выводит "недопустимое", если
     n не является центрированным шестиугольным числом или его иллюстрацией в виде
     многострочной прямоугольной строки в противном случае **/
    public static void hexLattice(int number) {
        // вычисляем модификатор, присваиваем его number
        for (int i = 1; number >= 1; i++) {
            if (number == 1) {
                number = i;
                break;
            }
            number -= i * 6;
        }
        // выводим сообщение о недопустимости
        if (number < 0) System.out.println("Invalid");

        // вывод иллюстрации шестиугольника с учётом свободных полей
        int space = 1 + (number - 1) * 2;
        for (int i = space; i > 0; i--) {
            System.out.println(" ".repeat(Math.abs(space + 1 - number)) + "o ".repeat(number));
            if (number <= i) number++;
            else number--;
        }
    }

}