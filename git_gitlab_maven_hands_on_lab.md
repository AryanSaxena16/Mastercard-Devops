# Git, GitLab, And Maven Hands-On Lab

Goal:

```text
Master the branch -> commit -> push -> merge request -> review -> merge workflow,
then understand Maven enough to build a deployable JAR.
```

Use this with your GitLab repository.

Important:

```text
Your GitLab screenshot says SSH is not configured.
Use HTTPS clone URL unless you add an SSH key to GitLab.
```

---

## 1. Core Git Concepts

### Repository

A Git repository is a project tracked by Git.

```text
Working directory = your files
Staging area      = changes selected for commit
Commit history    = saved snapshots
Remote            = GitLab/GitHub server copy
```

Useful command:

```bash
git status
```

Why:

```text
Before and after every Git action
-> check what changed
-> check branch name
-> avoid committing wrong files
```

---

### Branch

A branch is a separate line of work.

Enterprise teams usually do not commit directly to `main`.

Common branches:

```text
main        = stable production-ready code
develop     = integration branch, if used
feature/*   = new feature work
bugfix/*    = bug fixes
hotfix/*    = urgent production fix
release/*   = release preparation
```

Create a branch:

```bash
git switch -c feature/update-readme
```

Why:

```text
Need isolated work
-> create feature branch
-> make commits
-> push branch
-> open merge request
```

---

### Commit

A commit is a saved snapshot of staged changes.

```bash
git add README.md
git commit -m "Update project README"
```

Why:

```text
File changed
-> stage selected file
-> commit with meaningful message
-> preserve history
```

---

### Remote

A remote is the GitLab repository URL.

```bash
git remote -v
```

Why:

```text
Need push or pull
-> verify remote URL
-> confirm you are connected to correct GitLab project
```

---

## 2. First Hands-On: Clone The GitLab Repo

In GitLab:

```text
Code button
-> Clone with HTTPS
-> copy URL
```

Then run:

```bash
cd ~/Desktop
git clone <HTTPS_URL>
cd Mastercard-DevOps
git status
```

If Git asks for credentials:

```text
Username = your GitLab username
Password = GitLab token, not normal password, if your GitLab requires token auth
```

If SSH is not configured, do not use the SSH URL yet.

---

## 3. Hands-On: Make A File Change On A Feature Branch

Create a branch:

```bash
git switch -c feature/add-linux-notes
```

Create a file:

```bash
nano linux-notes.md
```

Add this content:

```markdown
# Linux Notes

## Useful Commands

- `pwd` shows current directory.
- `ls -la` lists files with permissions.
- `df -h` shows filesystem disk usage.
- `systemctl status nginx` checks nginx service status.
- `journalctl -u nginx -n 100 --no-pager` shows recent nginx logs.
```

Check changes:

```bash
git status
git diff
```

Stage and commit:

```bash
git add linux-notes.md
git commit -m "Add Linux notes"
```

Push branch:

```bash
git push -u origin feature/add-linux-notes
```

Why:

```text
Need to contribute change
-> create branch
-> edit file
-> inspect diff
-> stage
-> commit
-> push
-> open merge request
```

---

## 4. GitLab Merge Request Workflow

After pushing, GitLab usually shows:

```text
Create merge request
```

Use:

```text
Source branch: feature/add-linux-notes
Target branch: main
```

Merge request flow:

```text
Push branch
-> open merge request
-> reviewer checks code
-> pipeline/status checks run
-> comments fixed if needed
-> merge into main
```

Why:

```text
Enterprise teams protect main
-> changes are reviewed
-> tests must pass
-> history stays traceable
```

---

## 5. Branch Protection And Required Checks

Branch protection means rules on important branches like `main`.

Common rules:

```text
No direct push to main
Merge request required
Approval required
Pipeline must pass
Only maintainers can merge
```

Why:

```text
Protect production code
-> prevent accidental direct changes
-> require tests and review
-> keep main stable
```

---

## 6. Pull Latest Changes

Before starting work:

```bash
git switch main
git pull origin main
```

Then create your branch:

```bash
git switch -c feature/my-change
```

Why:

```text
Need fresh starting point
-> update local main
-> branch from latest code
-> reduce merge conflicts
```

---

## 7. Merge Conflict Hands-On

This lab creates a conflict safely using a practice file.

Start from main:

```bash
git switch main
git pull origin main
```

Create branch A:

```bash
git switch -c feature/conflict-a
nano conflict-demo.txt
```

Add:

```text
Environment=dev
```

Commit:

```bash
git add conflict-demo.txt
git commit -m "Add dev environment setting"
git switch main
git merge feature/conflict-a
```

Create branch B from older or current main:

```bash
git switch -c feature/conflict-b
nano conflict-demo.txt
```

Change the same line to:

```text
Environment=prod
```

Commit:

```bash
git add conflict-demo.txt
git commit -m "Change environment setting to prod"
```

Now create a conflict by merging main if main has a different version:

```bash
git merge main
```

If Git shows conflict markers, the file will look like:

```text
<<<<<<< HEAD
Environment=prod
=======
Environment=dev
>>>>>>> main
```

Resolve by choosing the correct final content, for example:

```text
Environment=prod
```

Then:

```bash
git add conflict-demo.txt
git commit
git status
```

Meaning:

```text
<<<<<<< HEAD = your branch version
======= = divider
>>>>>>> main = incoming branch version
```

Why:

```text
Two branches changed the same line
-> Git cannot decide automatically
-> human chooses correct final content
-> stage resolved file
-> commit merge resolution
```

---

## 8. Merge Vs Rebase

### Merge

```bash
git merge main
```

Meaning:

```text
Brings main into your branch and creates a merge commit if needed.
```

Why:

```text
Preserves full branch history
-> easier to see where work came together
```

### Rebase

```bash
git rebase main
```

Meaning:

```text
Replays your branch commits on top of latest main.
```

Why:

```text
Creates cleaner linear history
-> but rewrites commit history
```

Important:

```text
Do not rebase shared branches casually.
If your branch is already pushed and others use it, coordinate first.
```

Typical personal feature branch update:

```bash
git switch feature/my-change
git fetch origin
git rebase origin/main
```

If conflicts happen:

```bash
git status
# fix files
git add <file>
git rebase --continue
```

Abort rebase if needed:

```bash
git rebase --abort
```

---

## 9. Tags And Release Versioning

Tags mark important commits, usually releases.

Create annotated tag:

```bash
git tag -a v1.0.0 -m "Release v1.0.0"
```

Push tag:

```bash
git push origin v1.0.0
```

List tags:

```bash
git tag
```

Why:

```text
Release created
-> mark exact commit
-> build/deploy artifact from known version
-> rollback or audit later
```

Common version style:

```text
MAJOR.MINOR.PATCH

1.0.0 -> first stable release
1.1.0 -> new backward-compatible feature
1.1.1 -> bug fix
2.0.0 -> breaking change
```

---

## 10. Useful Git Commands

Check state:

```bash
git status
```

View changes:

```bash
git diff
```

View staged changes:

```bash
git diff --staged
```

View history:

```bash
git log --oneline --graph --decorate --all
```

Show branches:

```bash
git branch
git branch -a
```

Switch branch:

```bash
git switch <branch>
```

Create and switch branch:

```bash
git switch -c <branch>
```

Fetch remote updates:

```bash
git fetch origin
```

Pull current branch:

```bash
git pull
```

Push current branch:

```bash
git push
```

Push first time:

```bash
git push -u origin <branch>
```

---

## 11. Maven Project Structure

Common Java Maven project:

```text
project/
├── pom.xml
└── src/
    ├── main/
    │   └── java/
    │       └── com/example/App.java
    └── test/
        └── java/
            └── com/example/AppTest.java
```

Important file:

```text
pom.xml
```

Meaning:

```text
pom.xml = Maven project configuration
dependencies = external libraries
plugins = build tools
version = project artifact version
```

---

## 12. Maven Build Lifecycle

Common commands:

```bash
mvn compile
mvn test
mvn package
mvn verify
mvn install
```

Meaning:

```text
compile = compile source code
test = run unit tests
package = create JAR/WAR
verify = run checks to verify package
install = put artifact into local Maven repository
```

Why:

```text
Need deployable Java artifact
-> compile code
-> run tests
-> package JAR
-> verify build
```

Most common for creating a JAR:

```bash
mvn clean package
```

Output usually appears in:

```text
target/
```

---

## 13. Maven Dependency Conflicts

Show dependency tree:

```bash
mvn dependency:tree
```

Why:

```text
Build or runtime dependency issue
-> inspect dependency versions
-> find duplicate/conflicting libraries
-> fix pom.xml
```

Common symptoms:

```text
ClassNotFoundException
NoSuchMethodError
Dependency version conflict
Build fails downloading dependency
```

---

## 14. Skipping Tests Safely Vs Unsafely

Run tests:

```bash
mvn test
```

Package with tests:

```bash
mvn clean package
```

Skip running tests, but still compile tests:

```bash
mvn clean package -DskipTests
```

Skip compiling and running tests:

```bash
mvn clean package -Dmaven.test.skip=true
```

Meaning:

```text
-DskipTests = safer skip, test code still compiles
-Dmaven.test.skip=true = more aggressive, test code not compiled
```

Why:

```text
Pipeline emergency or temporary issue
-> skipping tests may be needed
-> document reason
-> do not make it normal practice
```

---

## 15. Full Final Workflow To Practice

Use this exact flow for your GitLab repo:

```bash
git clone <HTTPS_URL>
cd Mastercard-DevOps
git status

git switch -c feature/add-linux-notes
nano linux-notes.md
git status
git diff
git add linux-notes.md
git commit -m "Add Linux notes"
git push -u origin feature/add-linux-notes
```

Then in GitLab:

```text
Create merge request
-> target main
-> review changes
-> merge after approval/checks
```

After merge:

```bash
git switch main
git pull origin main
git tag -a v1.0.0 -m "Release v1.0.0"
git push origin v1.0.0
```

If Maven project exists:

```bash
mvn clean package
ls target/
```

Final mental model:

```text
Pull latest main
-> create branch
-> make file change
-> check diff
-> commit
-> push
-> merge request
-> review/checks
-> merge
-> tag release
-> build artifact
```

