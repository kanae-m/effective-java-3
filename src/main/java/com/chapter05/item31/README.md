# 項目31 APIの柔軟性向上のために境界ワイルドカードを使う

## `List<String>` は `List<Object>` のサブタイプではありません。

パラメータ化された型は不変です(項目28)。そのため、`List<String>` は `List<Object>` のサブタイプではありません。

`List<String>` が `List<Object>` のサブタイプではない理由は、実際にリストにインスタンスを追加してみるとわかります。`List<Object>` には `Sample` インスタンスを追加できますが、`List<String>` には `Sample` インスタンスを追加できません。つまり、`List<Object>` でできたことが `List<String>` ではできないことがあります。これは、**リスコフの置換原則**に反するため、`List<String>` は `List<Object>` のサブタイプではないことがわかります。

```java
List<Object> objectList = new ArrayList<>();
objectList.add("string");
objectList.add(new Sample());

List<String> stringList = new ArrayList<>();
stringList.add("string");
stringList.add(new Sample()); // コンパイルエラー
```

---

## 境界ワイルドカード型 (`? extends E`)

項目29で作成した `Stack<E>` クラスに、`Iterable` 型の引数を受け取って、それらをすべてスタックにプッシュするメソッドを追加したいと仮定します。

普通に書くと、以下のようになります。

```java
public void pushAll(Iterable<E> src) {
    for (E e : src) {
        push(e);
    }
}
```

これはコンパイル時にエラーや警告はおきませんが、`E` のサブタイプの `Iterable` を追加しようとすると、パラメータ化された型は不変のため、エラーが起きます。

```java
StackNotWildcard<Number> numberStackNotWildcard = new StackNotWildcard<>();
Iterable<Integer> integers = Set.of(1, 2, 3);
numberStackNotWildcard.pushAll(integers); // コンパイルエラー
```

```
'com.chapter05.item31.StackNotWildcard' の 'pushAll(java.lang.Iterable<javalang.Number>)' は '(java.lang.Iterable<java.lang.Integer>)' に適用できません
```

このエラーは、**境界ワイルドカード型**を使うと解決できます。`pushAll`メソッドの引数パラメータの型は、「`E` の `Iterable`」ではなく、「`E` の何らかのサブタイプの `Iterable`」にする必要がありますが、これは、境界ワイルドカード型を使って `Iterable<? extends E>` とあらわすことができます。この型を使って以下のように修正します。

```java
public void pushAll(Iterable<? extends E> src) {
    for (E e : src) {
        push(e);
    }
}
```

---

## 境界ワイルドカード型 (`? super E`)

次に、`popAll` メソッドについて考えます。普通に実装すると以下のようになります。

```java
public void popAll(Collection<E> dst) {
    while (!isEmpty()) {
        dst.add(pop());
    }
}
```

このコードも、コンパイル時にエラーや警告が起きませんが、実行時の引数に `E` のスーパークラスの `Collection` を引数に指定するとエラーが起きます。

```java
Collection<Object> objects = new ArrayList<>();
StackNotWildcard<Number> numberStackNotWildcard = new StackNotWildcard<>();
numberStackNotWildcard.popAll(objects); // コンパイルエラー
```

```
エラー: 不適合な型: Collection<Object>をCollection<Number>に変換できません:
numberStackNotWildcard.popAll(objects);
```

これもワイルドカード型を使うと解決できます。`popAll` メソッドの引数パラメータの型は、「`E` の `Collection`」ではなく、「`E` の何らかのスーパータイプの `Collection`」にする必要がありますが、これは、境界ワイルドカード型を使って `Collection<? super E>` とあらわすことができます。この型を使って以下のように修正します。

```java
public void popAll(Collection<? super E> dst) {
    while (!isEmpty()) {
        dst.add(pop());
    }
}
```

---

## PECS

`Stack` の例では、`pushAll` の `src` パラメータは `Stack` で使う `E` インスタンスを生産(produce)するので、`src` に対する適切な型は `Iterable<? extends E>` です。`popAll` の `dst` パラメータは `Stack` からの `E` インスタンスを消費(consume)するので、 `dst` に対する適切な型は、`Collection<? super E>` です。

`extends` を使うのか `super` を使うのかは、**PECS**という略語で覚えられます。PECSは **producer - extends, consumer - super** の頭文字をとっています。PECS略語は、ワイルドカード型の仕様の指針となる基本原則をとらえています。この原則は**Get&Put原則**と呼ばれています。

---

## 項目28 `Chooser` をワイルドカードに修正

項目28で宣言した `Chooser` コンストラクタは次のように実装されていました。

```java
public class Chooser<T> {

    private final List<T> choiceList;

    public Chooser(Collection<T> choices) {
        choiceList = new ArrayList<>(choices);
    }

}
```

この場合、`T` のサブタイプの `Collection` をコンストラクタの引数に指定するとエラーが起きます。

```java
List<Integer> integerList = List.of(1, 2, 3);
Chooser<Number> chooser28 = new Chooser<>(integerList); // コンパイルエラー
```

よって、ワイルドカード型を使って、以下のように修正すると解消されます。

```java
public class Chooser<T> {

    private final List<? extends T> choiceList;

    public Chooser(Collection<T> choices) {
        choiceList = new ArrayList<>(choices);
    }

}
```

---

## 項目30 `union` をワイルドカードに修正

項目30で実装した `union` メソッドもワイルドカードを使って柔軟性を与えることができます。

もともとの実装は、以下です。

```java
public static <E> Set<E> union(Set<E> s1, Set<E> s2) {
    Set<E> result = new HashSet<>(s1);
    result.addAll(s2);
    return result;
}
```

この場合、`E` のサブクラスの `Set` を引数に渡すことができません。

```java
Set<Integer> integerSet = Set.of(1, 2, 3);
Set<Double> doubleSet = Set.of(2.0, 4.0, 6.0);
Set<Number> numbers1 = union(integerSet, doubleSet);
```

よって、ワイルドカード型を使って、以下のように修正すると解消されます。

```java
public static <E> Set<E> union(Set<? extends E> s1, Set<? extends E> s2) {
    Set<E> result = new HashSet<>(s1);
    result.addAll(s2);
    return result;
}
```

---

## 項目30 `max` をワイルドカードに修正

項目30の渡された `Collection` から最大値を取得する `max` メソッドもワイルドカードを使用すると柔軟性を与えることができます。

もともとの実装は、以下です。

```java
public static <E extends Comparable<E>> E max(Collection<E> c) {
    if (c.isEmpty()) {
        throw new IllegalArgumentException("Empty collection");
    }
    E result = null;
    for (E e : c) {
        if (result == null || e.compareTo(result) > 0) {
            result = Objects.requireNonNull(e);
        }
    }
    return result;
}
```

`Comparable` を実装している `Parent` クラスがあり、そのクラスを継承する `Child` クラスがあるとします。

```java
public class Parent implements Comparable<Parent> {
    ・・・
}
```

```java
public class Child extends Parent {
    ・・・
}
```

先ほどの `max` メソッドに `Child` クラスの `Collection` を渡そうとすると、コンパイルエラーになります。

```java
Child maxChild = max(childList); // コンパイルエラー
```

ここで、ワイルドカードを使って `max` メソッドを以下のように修正すると、解決できます。

```java
public static <E extends Comparable<? super E>> E max(Collection<? extends E> c) {
    if (c.isEmpty()) {
        throw new IllegalArgumentException("Empty collection");
    }
    E result = null;
    for (E e : c) {
        if (result == null || e.compareTo(result) > 0) {
            result = Objects.requireNonNull(e);
        }
    }
    return result;
}
```

引数の `Collection` は `E` インスタンスを生成するので、型を `Collection<E>` から `Collection<? extends E>` に変更します。`Comparable<E>` は `E` と比較可能なものは `E` インスタンスを総日するので、パラメータ化された型 `Comparable<E>` は、`Comparable<E>` ではなく `Comparable<? super E>` を使います。

---

## 型パラメータとワイルドカード型のどちらを使えばいいか

以下の2つのメソッドを比較します。

```java
public static <E> void swap(List<E> list, int i, int j);
public static void swap(List<?> list, int i, int j);
```

public の API の場合は、2つ目のほうが単純で好ましいです。2つ目はどんなリストも渡せますし、メソッドはインデックスで指定された要素を交換します。一般に、型パラメータがメソッド宣言中に一度しか現れない場合は、ワイルドカードに置き換えたほうがいいです。しかし、2つ目の宣言で普通に実装するとコンパイルエラーが起きます。

```java
public static void swap(List<?> list, int i, int j) {
    list.set(i, list.set(j, list.get(i))); // コンパイルエラー
}
```

```
'java.util.List' の 'set(int, capture<?>)' は '(int, capture<?>)' に適用できません
```

しかし、型パラメータを使用したヘルパーメソッドを書くことで、解決できます。

```java
public static void swap(List<?> list, int i, int j) {
    swapHelper(list, i, j);
}

private static <E> void swapHelper(List<E> list, int i, int j) {
    list.set(i, list.set(j, list.get(j)));
}
```

こうすると、`list` が `List<E>` であることがわかるので、リストから取り出した値が `E` 型であり、 `E` 型の値をリストに戻すのが安全であるとわかっています。一方で、 `swap` メソッドを使う側は、この複雑な `private` メソッドを使用しているのはわからないので、使う側はシンプルになります。

---

## まとめ

広く使われるライブラリを書く場合、ワイルドカード型を適切に使うことは、ほぼ必須です。また、**producer - extends , consumer - super (PECS)** を覚えてください。