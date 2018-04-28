package pers.th.util.text

final token = new Token()

1.upto(999) {
    new Thread({
        try {
            synchronized (token) {
                System.out.println(Thread.currentThread().getName() + ":" + token.useKey(token.createKey()))
                System.out.println(Thread.currentThread().getName() + ":" + token.useKey(token.createKey()))
            }
        } catch (Exception e) {
            e.printStackTrace()
            System.exit(0)
        }
    }).start()
}