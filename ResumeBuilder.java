import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.Desktop;

public class ResumeBuilder {

    JFrame frame;
    JTextField nameField, emailField, phoneField, skillField;
    JTextArea eduArea, expArea, resultArea;
    JButton addSkillButton, generateButton, resetButton, saveButton;
    JComboBox<String> designDropdown;
    ArrayList<String> skills;

    ResumeBuilder() {
        frame = new JFrame("📝 Resume Builder");
        frame.setSize(700, 600);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(245, 245, 220));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titleLabel = new JLabel("🎯 Create Your Professional Resume");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(200, 10, 400, 30);
        frame.add(titleLabel);

        JLabel nameLabel = new JLabel("👤 Name:");
        nameLabel.setBounds(50, 50, 100, 25);
        frame.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(150, 50, 200, 25);
        frame.add(nameField);

        JLabel emailLabel = new JLabel("📧 Email:");
        emailLabel.setBounds(50, 90, 100, 25);
        frame.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(150, 90, 200, 25);
        frame.add(emailField);

        JLabel phoneLabel = new JLabel("📞 Phone:");
        phoneLabel.setBounds(50, 130, 100, 25);
        frame.add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(150, 130, 200, 25);
        frame.add(phoneField);

        JLabel skillLabel = new JLabel("💡 Skill:");
        skillLabel.setBounds(50, 170, 100, 25);
        frame.add(skillLabel);

        skillField = new JTextField();
        skillField.setBounds(150, 170, 150, 25);
        frame.add(skillField);

        addSkillButton = new JButton("➕ Add Skill");
        addSkillButton.setBounds(320, 170, 120, 25);
        frame.add(addSkillButton);

        JLabel eduLabel = new JLabel("🎓 Education:");
        eduLabel.setBounds(50, 210, 100, 25);
        frame.add(eduLabel);

        eduArea = new JTextArea();
        eduArea.setBounds(150, 210, 200, 50);
        frame.add(eduArea);

        JLabel expLabel = new JLabel("🏢 Experience:");
        expLabel.setBounds(50, 280, 100, 25);
        frame.add(expLabel);

        expArea = new JTextArea();
        expArea.setBounds(150, 280, 200, 50);
        frame.add(expArea);

        JLabel designLabel = new JLabel("🎨 Select Design:");
        designLabel.setBounds(50, 350, 120, 25);
        frame.add(designLabel);

        String[] designOptions = {"Classic", "Modern", "Minimal"};
        designDropdown = new JComboBox<>(designOptions);
        designDropdown.setBounds(180, 350, 150, 25);
        frame.add(designDropdown);

        generateButton = new JButton("📄 Generate HTML Resume");
        generateButton.setBounds(50, 400, 200, 30);
        frame.add(generateButton);

        resetButton = new JButton("🔄 Reset");
        resetButton.setBounds(270, 400, 100, 30);
        frame.add(resetButton);

        saveButton = new JButton("💾 Save as HTML");
        saveButton.setBounds(390, 400, 150, 30);
        frame.add(saveButton);

        resultArea = new JTextArea();
        resultArea.setBounds(50, 450, 580, 100);
        resultArea.setEditable(false);
        frame.add(resultArea);

        skills = new ArrayList<>();

        // Button Listeners
        addSkillButton.addActionListener(e -> {
            String skill = skillField.getText().trim();
            if (!skill.isEmpty()) {
                skills.add(skill);
                skillField.setText("");
                JOptionPane.showMessageDialog(frame, "Skill Added: " + skill);
            }
        });

        generateButton.addActionListener(e -> generateResume());
        resetButton.addActionListener(e -> resetFields());
        saveButton.addActionListener(e -> saveAsHtml());

        frame.setVisible(true);
    }

    // Generate Resume
    void generateResume() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String education = eduArea.getText().trim();
        String experience = expArea.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || education.isEmpty() || experience.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "⚠️ Please fill all fields!");
            return;
        }

        StringBuilder resume = new StringBuilder();
        resume.append("🌟 Resume Summary 🌟\n\n");
        resume.append("👤 Name: ").append(name).append("\n");
        resume.append("📧 Email: ").append(email).append("\n");
        resume.append("📞 Phone: ").append(phone).append("\n\n");

        resume.append("🎓 Education:\n").append(education).append("\n\n");
        resume.append("🏢 Experience:\n").append(experience).append("\n\n");

        resume.append("💡 Skills:\n");
        for (String skill : skills) {
            resume.append("- ").append(skill).append("\n");
        }

        resultArea.setText(resume.toString());
    }

    // Reset Fields
    void resetFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        skillField.setText("");
        eduArea.setText("");
        expArea.setText("");
        resultArea.setText("");
        skills.clear();
        JOptionPane.showMessageDialog(frame, "🔄 All fields have been reset!");
    }

    // Save Resume as HTML with Design Options
    void saveAsHtml() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String education = eduArea.getText().trim();
        String experience = expArea.getText().trim();
        String selectedDesign = (String) designDropdown.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || education.isEmpty() || experience.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "⚠️ Please fill all fields before saving!");
            return;
        }

        String htmlTemplate = getHtmlTemplate(selectedDesign, name, email, phone, education, experience, skills);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Resume.html"))) {
            writer.write(htmlTemplate);
            JOptionPane.showMessageDialog(frame, "💾 Resume saved successfully as Resume.html!");

            // Open HTML file in browser
            Desktop.getDesktop().browse(new File("Resume.html").toURI());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "❌ Error saving resume!");
        }
    }

    // Generate HTML Template based on Design Selection
    String getHtmlTemplate(String design, String name, String email, String phone, String education, String experience, ArrayList<String> skills) {
        StringBuilder html = new StringBuilder();
        
        String css = getCssForDesign(design);

        html.append("<html><head><style>").append(css).append("</style></head><body>");
        html.append("<div class='container'>");
        html.append("<h1>").append(name).append("</h1>");
        html.append("<p><b>Email:</b> ").append(email).append("</p>");
        html.append("<p><b>Phone:</b> ").append(phone).append("</p>");
        html.append("<h2>Education</h2><p>").append(education).append("</p>");
        html.append("<h2>Experience</h2><p>").append(experience).append("</p>");
        html.append("<h2>Skills</h2><ul>");
        for (String skill : skills) {
            html.append("<li>").append(skill).append("</li>");
        }
        html.append("</ul>");
        html.append("</div></body></html>");

        return html.toString();
    }

    // CSS for Different Design Options
    String getCssForDesign(String design) {
        switch (design) {
            case "Modern":
                return "body { background-color: #f2f2f2; font-family: Arial, sans-serif; } "
                        + ".container { background: white; padding: 20px; margin: 50px auto; width: 600px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }"
                        + "h1 { color: #333; } h2 { color: #444; } ul { list-style: none; } li { margin-bottom: 5px; }";
            
            case "Minimal":
                return "body { font-family: 'Times New Roman', serif; margin: 40px; background-color: #fff; } "
                        + ".container { width: 80%; margin: 0 auto; } "
                        + "h1, h2 { border-bottom: 1px solid #ccc; padding-bottom: 5px; } "
                        + "ul { padding-left: 20px; }";

            case "Classic":
            default:
                return "body { font-family: Georgia, serif; background-color: #f8f8f8; } "
                        + ".container { background: #fff; padding: 20px; margin: 50px auto; width: 600px; border: 1px solid #ddd; } "
                        + "h1 { color: #000; } h2 { color: #333; } ul { list-style-type: square; padding-left: 20px; }";
        }
    }

    public static void main(String[] args) {
        new ResumeBuilder();
    }
}
