package command_line_assignment;
import java.util.Scanner;


class Node {
    String name;
    boolean isDirectory;
    Node parent;
    Node firstChild;
    Node nextSibling;

    Node(String name, boolean isDirectory, Node parent) {
        this.name = name;
        this.isDirectory = isDirectory;
        this.parent = parent;
        this.firstChild = null;
        this.nextSibling = null;
    }
}

class FileSystem {
    private Node root;
    private Node current;

    public FileSystem() {
        root = new Node("~", true, null);
        current = root;
    }

    private Node findChild(String name) {
        Node child = current.firstChild;
        while (child != null) {
            if (child.name.equals(name)) {
                return child;
            }
            child = child.nextSibling;
        }
        return null;
    }

    public void mkdir(String name) {
        if (findChild(name) != null) {
            System.out.println("Direktori sudah ada.");
            return;
        }
        Node newNode = new Node(name, true, current);
        if (current.firstChild == null) {
            current.firstChild = newNode;
        } else {
            Node sibling = current.firstChild;
            while (sibling.nextSibling != null) {
                sibling = sibling.nextSibling;
            }
            sibling.nextSibling = newNode;
        }
    }

    public void mkfile(String name) {
        if (findChild(name) != null) {
            System.out.println("File sudah ada.");
            return;
        }
        Node newNode = new Node(name, false, current);
        if (current.firstChild == null) {
            current.firstChild = newNode;
        } else {
            Node sibling = current.firstChild;
            while (sibling.nextSibling != null) {
                sibling = sibling.nextSibling;
            }
            sibling.nextSibling = newNode;
        }
    }

    public void del(String name) {
        Node child = current.firstChild;
        Node prev = null;
        while (child != null) {
            if (child.name.equals(name)) {
                if (prev != null) {
                    prev.nextSibling = child.nextSibling;
                } else {
                    current.firstChild = child.nextSibling;
                }
                deleteNode(child);
                return;
            }
            prev = child;
            child = child.nextSibling;
        }
        System.out.println("File atau direktori tidak ditemukan.");
    }

    private void deleteNode(Node node) {
        if (node != null) {
            deleteNode(node.firstChild);
            deleteNode(node.nextSibling);
        }
    }

    public void dir() {
        Node child = current.firstChild;
        while (child != null) {
            System.out.println((child.isDirectory ? "[DIR] " : "[FILE] ") + child.name);
            child = child.nextSibling;
        }
    }

    public void cd(String name) {
        if (name.equals("..")) {
            if (current.parent != null) {
                current = current.parent;
            }
        } else {
            Node nextDir = findChild(name);
            if (nextDir != null && nextDir.isDirectory) {
                current = nextDir;
            } else {
                System.out.println("Direktori tidak ditemukan.");
            }
        }
    }

    public String getCurrentPath() {
        if (current == root) {
            return root.name;
        }

        StringBuilder path = new StringBuilder();
        Node temp = current;
        while (temp != root) {
            path.insert(0, "/" + temp.name);
            temp = temp.parent;
        }
        return root.name + path.toString();
    }
}

public class Main {
    public static void main(String[] args) {
        FileSystem fs = new FileSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(fs.getCurrentPath() + "\n$ ");
            String input = scanner.nextLine();
            String[] tokens = input.split(" ");
            String command = tokens[0];

            if (command.equals("exit")) {
                break;
            }

            String argument = tokens.length > 1 ? tokens[1] : null;

            switch (command) {
                case "mkdir":
                    if (argument == null) {
                        System.out.println("Error: mkdir membutuhkan argumen.");
                    } else {
                        fs.mkdir(argument);
                    }
                    break;
                case "mkfile":
                    if (argument == null) {
                        System.out.println("Error: mkfile membutuhkan argumen.");
                    } else {
                        fs.mkfile(argument);
                    }
                    break;
                case "del":
                    if (argument == null) {
                        System.out.println("Error: del membutuhkan argumen.");
                    } else {
                        fs.del(argument);
                    }
                    break;
                case "cd":
                    if (argument == null) {
                        System.out.println("Error: cd membutuhkan argumen.");
                    } else {
                        fs.cd(argument);
                    }
                    break;
                case "dir":
                    fs.dir();
                    break;
                default:
                    System.out.println("Perintah tidak dikenal.");
                    break;
            }
        }
        scanner.close();
    }
}
