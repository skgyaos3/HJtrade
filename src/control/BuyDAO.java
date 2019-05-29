package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import com.itextpdf.text.log.SysoCounter;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import model.BuyVO;
import model.ImportionVO;
import model.ProductVO;
import model.StockVO;

public class BuyDAO {
	// 입고 목록
	public ArrayList<BuyVO> getbuyTotalList() throws Exception {
		ArrayList<BuyVO> list = new ArrayList<>();

		String sql = "select b.b_no, b.b_buyDate, b.b_date, i.i_name, b.b_code, p.p_type, p.p_origin, p.p_brand, p.p_part, b.b_number, b.b_kg , b.b_cost, b.b_totalmoney,s.s_state"
				+ " from buy b, product p, importion i, stock s" + " where b.p_no = p.p_no and b.i_no = i.i_no and b.s_no = s.s_no ";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BuyVO bVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {

				bVo = new BuyVO();
				bVo.setB_no(rs.getInt("B_no"));
				bVo.setB_buyDate(rs.getString("B_buyDate"));
				bVo.setB_date(rs.getString("B_date"));
				bVo.setI_name(rs.getString("I_name"));
				bVo.setB_code(rs.getString("B_code"));
				bVo.setP_type(rs.getString("P_type"));
				bVo.setP_origin(rs.getString("P_origin"));
				bVo.setP_brand(rs.getString("P_brand"));
				bVo.setP_part(rs.getString("P_part"));
				bVo.setB_number(rs.getInt("B_number"));
				bVo.setB_kg(rs.getDouble("B_kg"));
				bVo.setB_cost(rs.getInt("B_cost"));
				bVo.setB_totalMoney(rs.getInt("B_totalMoney"));

				list.add(bVo);

			}

		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {

			}
		}
		return list;
	}

	// 입고 등록
	public void getBuyRegiste(BuyVO bvo) throws Exception {

		String sql = "insert into buy"
				+ "(B_no , B_buyDate , B_date, B_code ,B_number, B_kg, B_cost, B_totalmoney, I_no, P_no, S_no)"
				+ "values" + "(buy_seq.nextval, ?, sysdate,?,?,?,?,?,?,?,stock_seq.nextval-1)";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, bvo.getB_buyDate());
			pstmt.setString(2, bvo.getB_code());
			pstmt.setInt(3, bvo.getB_number());
			pstmt.setDouble(4, bvo.getB_kg());
			pstmt.setInt(5, bvo.getB_cost());
			pstmt.setDouble(6, bvo.getB_kg() * bvo.getB_cost());
			pstmt.setInt(7, bvo.getI_no());
			pstmt.setInt(8, bvo.getP_no());
			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("입고 등록");
				alert.setHeaderText("입고 등록 완료");
				alert.setContentText("입고 완료");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("입고 등록");
				alert.setHeaderText("입고 등록 실패");
				alert.setContentText("입고등록 실패!!");
				alert.showAndWait();
			}

		} catch (SQLException e) {
			System.out.println("e=[" + e + "]1");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// 데이터베이스와의 연결에 사용되었던 오브젝트를해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {

			}
		}
	}

	//재고 목록
	public ArrayList<StockVO> getStockTotalList() {
		ArrayList<StockVO> list = new ArrayList<>();

		String sql = "select s.s_no, b.b_date, b.b_code, p.p_type, p.p_origin, p.p_brand, p.p_part,"
				+ " s.s_number, s.s_kg, s.s_cost, s.s_totalMoney, s.s_state" + " from stock s, buy b, product p"
				+ " where s.s_no = b.s_no and s.p_no = p.p_no and s.s_no = b.s_no" + " order by s.s_no";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StockVO sVo = null;

		try {

			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				// 인스턴스 생성
				sVo = new StockVO();
				// 쿼리문을 날리고 얻은 결과에서 값을 가져와 객체의 필드값을 설정한다.
				sVo.setS_no(rs.getInt("s_no"));
				sVo.setB_date(rs.getString("b_date"));
				sVo.setB_code(rs.getString("b_code"));
				sVo.setP_type(rs.getString("p_type"));
				sVo.setP_origin(rs.getString("p_origin"));
				sVo.setP_brand(rs.getString("p_brand"));
				sVo.setP_part(rs.getString("p_part"));
				sVo.setS_number(rs.getInt("s_number"));
				sVo.setS_kg(rs.getDouble("s_kg"));
				sVo.setS_cost(rs.getInt("s_cost"));
				sVo.setS_totalMoney(rs.getInt("s_totalMoney"));
				sVo.setS_state(rs.getString("s_state"));

				// 필드값을 설정해준후 arraylist배열에 객체를 추가한다.
				list.add(sVo);
			}

		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
			}
		}
		return list;
	}

	// 재고등록
	public boolean getStock(String s_number, String s_kg, String s_cost, String s_state, int p_no, int b_no) {

		System.out.println(p_no);
		String sql = "insert into stock" + "(s_no, s_number, s_kg, s_cost, s_totalmoney ,s_state, p_no)"
				+ " values " + "(stock_seq.nextval, ?, ?, ?, ?, ?, ?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		boolean StockUpdateSucess = false;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);	
			pstmt.setInt(1, Integer.parseInt(s_number));
			pstmt.setDouble(2, Double.parseDouble(s_kg));
			pstmt.setInt(3, Integer.parseInt(s_cost));
			pstmt.setDouble(4, Double.parseDouble(s_kg) * Integer.parseInt(s_cost));
			pstmt.setString(5, s_state);
			pstmt.setInt(6, p_no);

			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("출고 확인");
				alert.setHeaderText(" 출고 완료.");
				alert.setContentText("출고 성공!!!");
				alert.showAndWait();
				StockUpdateSucess = true;
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("출고 확인");
				alert.setHeaderText("출고 실패.");
				alert.setContentText("출고 실패!!!");
				alert.showAndWait();
			}
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return StockUpdateSucess;
	}

	// 데이터 베이스에서 상품 테이블의 컬럼의 갯수
	public ArrayList<String> getBuyColumnName() throws Exception {
		ArrayList<String> columnName = new ArrayList<String>();

		String sql = "select* from buy";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// Result setMetaData 객체 변순 선언
		ResultSetMetaData rsmd = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();
			for (int i = 1; i <= cols; i++) {
				columnName.add(rsmd.getColumnName(i));
			}
		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
				System.out.println(se);
			}
		}
		return columnName;
	}

	// 거래처 상호명 가져오기
	public ArrayList<String> getImportion() throws Exception {

		ArrayList<String> list = new ArrayList();

		String sql = "select i_name from Importion";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ImportionVO ivo = null;

		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담아줄 그릇
			pstmt = con.prepareStatement(sql);
			// jvo에서 변수들을 가져와서 sql문에 넣어준다.
			rs = pstmt.executeQuery();
			// sql을 날리고 불러온 값이 있으면 로그인결과변수 true
			while (rs.next()) {

				list.add(rs.getString("i_name"));
			}

		} catch (SQLException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		} finally {

			try {
				// 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}

		return list;

	}

	// 상호명으로 번호 가져오기
	public String getImportionNum(String i_name) throws Exception {

		String sql = "select i_no from Importion where i_name = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String i_no = "";

		try {

			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, i_name);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				i_no = rs.getString("i_no");
			}

		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
			}
		}
		return i_no;
	}

	// 콤보박스로 상품번호 가져오기
	public int getProductNumber(String origin, String brand, String part) throws Exception {

		String sql = "select p_no from product where p_origin=? and p_brand=? and p_part=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int result = 0;

		try {

			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, origin);
			pstmt.setString(2, brand);
			pstmt.setString(3, part);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt("p_no");
			}

		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
			}
		}
		return result;
	}

	public ArrayList<String> getOrigin() throws Exception {

		ArrayList<String> list = new ArrayList();

		String sql = "select DISTINCT p_origin from Product";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProductVO pvo = null;

		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담아줄 그릇
			pstmt = con.prepareStatement(sql);
			// jvo에서 변수들을 가져와서 sql문에 넣어준다.
			rs = pstmt.executeQuery();
			// sql을 날리고 불러온 값이 있으면 로그인결과변수 true
			while (rs.next()) {
				list.add(rs.getString("p_origin"));
			}

		} catch (SQLException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		} finally {

			try {
				// 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}

		return list;

	}

	// 상품 선택시 브랜드 정보 가져오기
	public ArrayList<String> getProductInfo() throws Exception {

		ArrayList<String> list = new ArrayList();

		String sql = "select DISTINCT p_brand from product";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProductVO pvo = null;

		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담아줄 그릇
			pstmt = con.prepareStatement(sql);
			// jvo에서 변수들을 가져와서 sql문에 넣어준다.
			rs = pstmt.executeQuery();
			// sql을 날리고 불러온 값이 있으면 로그인결과변수 true
			while (rs.next()) {

				list.add(rs.getString("p_brand"));
			}

		} catch (SQLException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		} finally {

			try {
				// 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}

		return list;

	}

	// 소 클릭시 소 부위 가져오기
	public ArrayList<String> getcowPart() throws Exception {

		ArrayList<String> list = new ArrayList();

		String sql = "select DISTINCT p_part from product where p_type ='소'";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProductVO pvo = null;

		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담아줄 그릇
			pstmt = con.prepareStatement(sql);
			// jvo에서 변수들을 가져와서 sql문에 넣어준다.
			rs = pstmt.executeQuery();
			// sql을 날리고 불러온 값이 있으면 로그인결과변수 true
			while (rs.next()) {

				list.add(rs.getString("p_part"));
			}

		} catch (SQLException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		} finally {

			try {
				// 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}

		return list;

	}

	public ArrayList<String> getpigPart() throws Exception {

		ArrayList<String> list = new ArrayList();

		String sql = "select DISTINCT p_part from product where p_type ='돼지'";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProductVO pvo = null;

		try {
			// DB연동
			con = DBUtil.getConnection();
			// sql문을 담아줄 그릇
			pstmt = con.prepareStatement(sql);
			// jvo에서 변수들을 가져와서 sql문에 넣어준다.
			rs = pstmt.executeQuery();
			// sql을 날리고 불러온 값이 있으면 로그인결과변수 true
			while (rs.next()) {

				list.add(rs.getString("p_part"));
			}

		} catch (SQLException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		} finally {

			try {
				// 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}

		return list;

	}

}