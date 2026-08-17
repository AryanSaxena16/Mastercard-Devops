# Student Branch DevOps Practice

This file is for practicing the GitLab branch-to-merge-request workflow.

## Student Details

Name: Aditya
Branch name: your-name-branch
Purpose: Git hands-on practice

## Git Workflow Practiced

- Create or switch to a personal branch.
- Add a new file.
- Check file changes with `git status` and `git diff`.
- Stage the file with `git add`.
- Commit the file with a meaningful message.
- Push the branch to GitLab.
- Create a merge request into `main`.

## Linux Revision Notes

Useful Linux commands:

```bash
pwd
ls -la
df -h
du -sh /var/log
systemctl status nginx
journalctl -u nginx -n 100 --no-pager
ss -tulnp
curl -v http://localhost:8080/health
```

## Git Commands Used

```bash
git status
git branch
git switch <branch-name>
git add student_branch_devops_practice.md
git commit -m "Add student branch DevOps practice file"
git push -u origin <branch-name>
```

## Maven Notes

Common Maven lifecycle commands:

```bash
mvn compile
mvn test
mvn package
mvn verify
mvn install
```

Most common build command:

```bash
mvn clean package
```

The deployable JAR is usually created under:

```text
target/
```

## Merge Conflict Practice Line

Use this line later to practice conflicts:

```text
Environment=dev
```

## Final Checklist

- [ ] File created on personal branch
- [ ] `git status` checked
- [ ] File staged
- [ ] Commit created
- [ ] Branch pushed
- [ ] Merge request opened
- [ ] Merge request reviewed
- [ ] Branch merged into `main`

