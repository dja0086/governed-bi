# S-04a: limits of the AWS account type, checked before building (D-027)

Date: 2026-11-21. Source: AWS Account Management guide pages "Compare sign-up options", "Supported AWS services for Sign up for AWS (new)", "AWS Regions for your projects", "Activate advanced AWS features", read in full.

The owner's account was created with "Sign up for AWS (new)": a project account owned by an AWS Builder ID, no root user. MFA registered on the Builder ID by the owner (owner's report).

| Need of the extraction service | Status on this account type |
|---|---|
| Region | Fixed by AWS from the contact address: us-east-2 for the Americas. Cannot be changed without activating advanced features |
| VPC, Secrets Manager, ECS, ECR, EventBridge, KMS, CloudWatch, S3, CloudFormation, Lambda, API Gateway, SQS, Step Functions, Batch, CloudTrail | Listed as supported |
| Fargate, NAT gateway, Elastic IP, EventBridge Scheduler | Not mentioned on the supported-services page. Unverified; test by deploying a minimal stack |
| IAM roles | Allowed for programmatic access only; AWS manages the organization's control policies |
| Regulated workloads (HIPAA, FedRAMP) | Not supported on this account type |

Activating advanced features: irreversible, removes spend limits, creates a root user and management account, opens other Regions and custom policies. Requires the account to be upgraded to the paid plan first.

Decision pending with the owner: do not activate now. Triggers for activating later: a client outside the Americas, a client that requires its own Region or a compliance designation, or a needed service that the test shows is blocked.

Unverified: which plan (free or paid) the account is on, and that plan's time and credit limits.
